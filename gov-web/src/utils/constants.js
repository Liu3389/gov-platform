// 菜单配置常量
export const MENU_LIST = [
  { name: '事项管理', code: 'item_manage', url: '/item', icon: 'list', sort: 2 },
  { name: '事项列表', code: 'item_list', url: '/item/list', icon: 'table', sort: 1 },
  { name: '事项录入', code: 'item_add', url: '/item/add', icon: 'edit', sort: 2 },
  { name: '办件管理', code: 'case_manage', url: '/case', icon: 'folder', sort: 3 },
  { name: '待办任务', code: 'todo_list', url: '/case/todo', icon: 'todo', sort: 1 },
  { name: '已办任务', code: 'done_list', url: '/case/done', icon: 'done', sort: 2 },
  { name: '证照管理', code: 'license_manage', url: '/license', icon: 'card', sort: 4 },
  { name: '投诉建议', code: 'complaint_manage', url: '/complaint', icon: 'message', sort: 5 },
  { name: '系统管理', code: 'system_manage', url: '/system', icon: 'setting', sort: 6 },
  { name: '用户管理', code: 'user_manage', url: '/system/user', icon: 'user', sort: 1 },
  { name: '角色管理', code: 'role_manage', url: '/system/role', icon: 'role', sort: 2 },
  { name: '部门管理', code: 'dept_manage', url: '/system/dept', icon: 'dept', sort: 3 },
]

// 办件状态映射
export const CASE_STATUS = {
  0: { label: '待受理', type: 'info' },
  1: { label: '受理中', type: 'warning' },
  2: { label: '审批中', type: 'primary' },
  3: { label: '已办结', type: 'success' },
  4: { label: '已驳回', type: 'danger' },
  5: { label: '已撤回', type: 'info' },
}

// 事项类型映射
export const ITEM_TYPE = {
  1: '行政许可',
  2: '公共服务',
  3: '行政确认',
}
