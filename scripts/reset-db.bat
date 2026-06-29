@echo off
REM ============================================
REM 一键重置环境（已迁移 -> 请使用 db/reset.ps1）
REM 用法: powershell -ExecutionPolicy Bypass -File db/reset.ps1
REM ============================================
cd /d "%~dp0.."
powershell -ExecutionPolicy Bypass -File db/reset.ps1
