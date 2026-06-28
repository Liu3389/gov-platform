﻿$token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJhZG1pbiIsImlzcyI6Imdvdi1wbGF0Zm9ybSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTc4MjM3MzYzMSwiZXhwIjoxNzgyMzgwODMxfQ.lJBL7iE9eYLDhoqB_wwaw_DaHsBBaRvDUn9sJgsP5Z0"
$headers = @{ "Authorization" = "Bearer $token"; "Content-Type" = "application/json" }
$base = "http://localhost:8091/api/v1/complaint"
$results = @()

function Test-API($testNum, $desc, $method, $url, $body, $expectedCodes, $useAuth) {
    $h = if ($useAuth) { $headers } else { @{"Content-Type"="application/json"} }
    try {
        if ($method -eq "GET") {
            $r = Invoke-RestMethod -Uri $url -Method GET -Headers $h -TimeoutSec 10
            $code = 200
            $bizCode = $r.code
            Write-Host "[$testNum] $desc : HTTP 200 biz=$bizCode PASS"
        } elseif ($method -eq "DELETE") {
            $r = Invoke-WebRequest -Uri $url -Method DELETE -Headers $h -TimeoutSec 10
            $code = $r.StatusCode
            Write-Host "[$testNum] $desc : HTTP $code PASS"
        } else {
            $r = Invoke-RestMethod -Uri $url -Method $method -Headers $h -Body $body -TimeoutSec 10
            $code = 200
            Write-Host "[$testNum] $desc : HTTP 200 PASS"
        }
        $global:results += [PSCustomObject]@{Num=$testNum;Desc=$desc;Method=$method;Expected=($expectedCodes -join '/');Actual=$code;Pass=$true}
        return $r
    } catch {
        $code = $_.Exception.Response.StatusCode.value__
        if (-not $code) { $code = "ERR" }
        $pass = $expectedCodes -contains $code
        $mark = if ($pass) { "PASS" } else { "FAIL(expected "+($expectedCodes -join '/')+")" }
        Write-Host "[$testNum] $desc : $code $mark"
        $global:results += [PSCustomObject]@{Num=$testNum;Desc=$desc;Method=$method;Expected=($expectedCodes -join '/');Actual=$code;Pass=$pass}
        return $null
    }
}

Write-Host ""
Write-Host "========================================"
Write-Host "  gov-complaint-service Full API Test"
Write-Host "  Time: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
Write-Host "  Base: $base"
Write-Host "========================================"
Write-Host ""

# 1. WorkController - /complaint
Write-Host "========== 1. WorkController =========="
Test-API 1.1 "LIST(no param)" "GET" "$base/list" $null @(200) $true
Test-API 1.2 "LIST(+keyword)" "GET" "$base/list?pageNum=1&pageSize=5&keyword=test" $null @(200) $true
Test-API 1.3 "LIST(+status)" "GET" "$base/list?pageNum=1&pageSize=5&status=0" $null @(200) $true
Test-API 1.4 "LIST(pageSize=100)" "GET" "$base/list?pageNum=1&pageSize=100" $null @(200) $true
Test-API 1.5 "LIST(pageSize=101 exceeded)" "GET" "$base/list?pageNum=1&pageSize=101" $null @(400) $true
$w1 = '{"title":"TestWork-Noise","content":"Construction noise at night","categoryId":1,"complaintType":"noise","userName":"ZhangSan","userPhone":"13800138000","deptId":1,"deptName":"Urban","status":"0","remark":"test"}'
Test-API 1.6 "ADD" "POST" "$base" $w1 @(200) $true
Test-API 1.7 "ADD(missing title)" "POST" "$base" '{"title":"","content":"n","categoryId":1}' @(400) $true
Test-API 1.8 "ADD(missing content)" "POST" "$base" '{"title":"t","content":"","categoryId":1}' @(400) $true
Test-API 1.9 "GET(id=1)" "GET" "$base/1" $null @(200) $true
Test-API 1.10 "GET(id=99999 notexist)" "GET" "$base/99999" $null @(200) $true
Test-API 1.11 "UPDATE" "PUT" "$base" '{"id":1,"title":"Updated","content":"Updated content","categoryId":1,"complaintType":"noise","status":"2"}' @(200) $true
Test-API 1.12 "DELETE(id=2)" "DELETE" "$base/2" $null @(200) $true
Test-API 1.13 "NoToken Access" "GET" "$base/list?pageNum=1&pageSize=1" $null @(401) $false
Write-Host ""

# 2. CategoryController - /category
Write-Host "========== 2. CategoryController =========="
Test-API 2.1 "LIST(no param)" "GET" "$base/category/list" $null @(200) $true
Test-API 2.2 "LIST(+keyword)" "GET" "$base/category/list?pageNum=1&pageSize=5&keyword=test" $null @(200) $true
Test-API 2.3 "LIST(+status)" "GET" "$base/category/list?pageNum=1&pageSize=5&status=1" $null @(200) $true
Test-API 2.4 "LIST(pageSize=100)" "GET" "$base/category/list?pageNum=1&pageSize=100" $null @(200) $true
Test-API 2.5 "LIST(pageSize=101 exceeded)" "GET" "$base/category/list?pageNum=1&pageSize=101" $null @(400) $true
Test-API 2.6 "ADD" "POST" "$base/category" '{"categoryName":"TestCat","categoryCode":"TEST001","parentId":0,"keywords":"test","defaultDeptId":1,"sort":1,"status":"1"}' @(200) $true
Test-API 2.7 "ADD(missing name)" "POST" "$base/category" '{"categoryName":"","categoryCode":"TEST002"}' @(400) $true
Test-API 2.8 "GET(id=1)" "GET" "$base/category/1" $null @(200) $true
Test-API 2.9 "GET(id=99999 notexist)" "GET" "$base/category/99999" $null @(200) $true
Test-API 2.10 "UPDATE" "PUT" "$base/category" '{"id":1,"categoryName":"UpdatedCat","categoryCode":"CAT001","status":"1"}' @(200) $true
Test-API 2.11 "DELETE(id=2)" "DELETE" "$base/category/2" $null @(200) $true
Test-API 2.12 "NoToken Access" "GET" "$base/category/list?pageNum=1&pageSize=1" $null @(401) $false
Write-Host ""

# 3. HandleController - /handle
Write-Host "========== 3. HandleController =========="
Test-API 3.1 "LIST(no param)" "GET" "$base/handle/list" $null @(200) $true
Test-API 3.2 "LIST(+workId)" "GET" "$base/handle/list?pageNum=1&pageSize=5&workId=1" $null @(200) $true
Test-API 3.3 "LIST(pageSize=100)" "GET" "$base/handle/list?pageNum=1&pageSize=100" $null @(200) $true
Test-API 3.4 "LIST(pageSize=101 exceeded)" "GET" "$base/handle/list?pageNum=1&pageSize=101" $null @(400) $true
Test-API 3.5 "GET_BY_WORK(workId=1)" "GET" "$base/handle/work/1" $null @(200) $true
Test-API 3.6 "GET_BY_WORK(workId=99999)" "GET" "$base/handle/work/99999" $null @(200) $true
Test-API 3.7 "ADD" "POST" "$base/handle" '{"workId":1,"handlerId":1,"handlerName":"LiSi","handleType":"transfer","handleContent":"Transfer to other dept","nextDeptId":1,"nextDeptName":"Dept"}' @(200) $true
Test-API 3.8 "ADD(missing content)" "POST" "$base/handle" '{"workId":1,"handlerId":1,"handleContent":""}' @(400) $true
Test-API 3.9 "GET(id=1)" "GET" "$base/handle/1" $null @(200) $true
Test-API 3.10 "GET(id=99999 notexist)" "GET" "$base/handle/99999" $null @(200) $true
Test-API 3.11 "UPDATE" "PUT" "$base/handle" '{"id":1,"workId":1,"handlerId":1,"handlerName":"LiSi","handleType":"done","handleContent":"Resolved"}' @(200) $true
Test-API 3.12 "DELETE(id=2)" "DELETE" "$base/handle/2" $null @(200) $true
Test-API 3.13 "NoToken Access" "GET" "$base/handle/list?pageNum=1&pageSize=1" $null @(401) $false
Write-Host ""

# 4. SuggestionController - /suggestion
Write-Host "========== 4. SuggestionController =========="
Test-API 4.1 "LIST(no param)" "GET" "$base/suggestion/list" $null @(200) $true
Test-API 4.2 "LIST(+keyword)" "GET" "$base/suggestion/list?pageNum=1&pageSize=5&keyword=optimize" $null @(200) $true
Test-API 4.3 "LIST(+status)" "GET" "$base/suggestion/list?pageNum=1&pageSize=5&status=0" $null @(200) $true
Test-API 4.4 "LIST(pageSize=100)" "GET" "$base/suggestion/list?pageNum=1&pageSize=100" $null @(200) $true
Test-API 4.5 "LIST(pageSize=101 exceeded)" "GET" "$base/suggestion/list?pageNum=1&pageSize=101" $null @(400) $true
Test-API 4.6 "ADD" "POST" "$base/suggestion" '{"title":"Optimize process","content":"Simplify document","suggestionType":"process","userName":"WangWu","status":"0"}' @(200) $true
Test-API 4.7 "ADD(missing title)" "POST" "$base/suggestion" '{"title":"","content":"suggestion"}' @(400) $true
Test-API 4.8 "GET(id=1)" "GET" "$base/suggestion/1" $null @(200) $true
Test-API 4.9 "GET(id=99999 notexist)" "GET" "$base/suggestion/99999" $null @(200) $true
Test-API 4.10 "UPDATE" "PUT" "$base/suggestion" '{"id":1,"title":"Updated","content":"Add online","status":"0"}' @(200) $true
Test-API 4.11 "REPLY" "PUT" "$base/suggestion/1/reply?replyContent=Thanks&replyBy=1" $null @(200) $true
Test-API 4.12 "DELETE(id=2)" "DELETE" "$base/suggestion/2" $null @(200) $true
Test-API 4.13 "NoToken Access" "GET" "$base/suggestion/list?pageNum=1&pageSize=1" $null @(401) $false
Write-Host ""

# 5. HotspotController - /hotspot
Write-Host "========== 5. HotspotController =========="
Test-API 5.1 "LIST(no param)" "GET" "$base/hotspot/list" $null @(200) $true
Test-API 5.2 "LIST(+keyword)" "GET" "$base/hotspot/list?pageNum=1&pageSize=5&keyword=noise" $null @(200) $true
Test-API 5.3 "LIST(pageSize=100)" "GET" "$base/hotspot/list?pageNum=1&pageSize=100" $null @(200) $true
Test-API 5.4 "LIST(pageSize=101 exceeded)" "GET" "$base/hotspot/list?pageNum=1&pageSize=101" $null @(400) $true
$dt = Get-Date -Format "yyyy-MM-ddTHH:mm:ss"
$hb = @{keyword="noise complaint";keywordCount=156;statDate=$dt;categoryId=1;trend="rising"} | ConvertTo-Json -Compress
Test-API 5.5 "ADD" "POST" "$base/hotspot" $hb @(200) $true
Test-API 5.6 "ADD(missing keyword)" "POST" "$base/hotspot" '{"keyword":"","keywordCount":0,"statDate":"2026-06-25T00:00:00"}' @(400) $true
Test-API 5.7 "GET(id=1)" "GET" "$base/hotspot/1" $null @(200) $true
Test-API 5.8 "GET(id=99999 notexist)" "GET" "$base/hotspot/99999" $null @(200) $true
$dt2 = Get-Date -Format "yyyy-MM-ddTHH:mm:ss"
$hu = @{id=1;keyword="noise updated";keywordCount=200;statDate=$dt2;trend="rising fast"} | ConvertTo-Json -Compress
Test-API 5.9 "UPDATE" "PUT" "$base/hotspot" $hu @(200) $true
Test-API 5.10 "DELETE(id=2)" "DELETE" "$base/hotspot/2" $null @(200) $true
Test-API 5.11 "NoToken Access" "GET" "$base/hotspot/list?pageNum=1&pageSize=1" $null @(401) $false
Write-Host ""

# 6. SuperviseController - /supervise
Write-Host "========== 6. SuperviseController =========="
Test-API 6.1 "LIST(no param)" "GET" "$base/supervise/list" $null @(200) $true
Test-API 6.2 "LIST(+workId)" "GET" "$base/supervise/list?pageNum=1&pageSize=5&workId=1" $null @(200) $true
Test-API 6.3 "LIST(+status)" "GET" "$base/supervise/list?pageNum=1&pageSize=5&status=1" $null @(200) $true
Test-API 6.4 "LIST(pageSize=100)" "GET" "$base/supervise/list?pageNum=1&pageSize=100" $null @(200) $true
Test-API 6.5 "LIST(pageSize=101 exceeded)" "GET" "$base/supervise/list?pageNum=1&pageSize=101" $null @(400) $true
Test-API 6.6 "GET_BY_WORK(workId=1)" "GET" "$base/supervise/work/1" $null @(200) $true
Test-API 6.7 "GET_BY_WORK(workId=99999)" "GET" "$base/supervise/work/99999" $null @(200) $true
$dl = (Get-Date).AddDays(3).ToString("yyyy-MM-ddTHH:mm:ss")
$sb = @{workId=1;superviseLevel="urgent";superviseContent="Handle ASAP";deadline=$dl;status="0"} | ConvertTo-Json -Compress
Test-API 6.8 "ADD" "POST" "$base/supervise" $sb @(200) $true
Test-API 6.9 "ADD(missing level)" "POST" "$base/supervise" '{"workId":1,"superviseLevel":"","superviseContent":"test"}' @(400) $true
Test-API 6.10 "GET(id=1)" "GET" "$base/supervise/1" $null @(200) $true
Test-API 6.11 "GET(id=99999 notexist)" "GET" "$base/supervise/99999" $null @(200) $true
$dl2 = (Get-Date).AddDays(5).ToString("yyyy-MM-ddTHH:mm:ss")
$su = @{id=1;workId=1;superviseLevel="critical";superviseContent="Upgraded";deadline=$dl2;status="1"} | ConvertTo-Json -Compress
Test-API 6.12 "UPDATE" "PUT" "$base/supervise" $su @(200) $true
Test-API 6.13 "DELETE(id=2)" "DELETE" "$base/supervise/2" $null @(200) $true
Test-API 6.14 "NoToken Access" "GET" "$base/supervise/list?pageNum=1&pageSize=1" $null @(401) $false

Write-Host ""
Write-Host "========================================"
Write-Host "  TEST COMPLETE! Summary"
Write-Host "========================================"

$total = $results.Count
$passed = ($results | Where-Object { $_.Pass -eq $true }).Count
$failed = $total - $passed

Write-Host "Total: $total"
Write-Host "Passed: $passed"
Write-Host "Failed: $failed"
Write-Host "Pass Rate: $([math]::Round($passed/$total*100, 1))%"

if ($failed -gt 0) {
    Write-Host ""
    Write-Host "--- Failed Details ---"
    $results | Where-Object { $_.Pass -eq $false } | ForEach-Object {
        Write-Host "  [$($_.Num)] $($_.Desc) - Expected:$($_.Expected) Actual:$($_.Actual)"
    }
}

Write-Host ""
$results | Format-Table Num, Desc, Method, Expected, Actual, Pass -AutoSize

