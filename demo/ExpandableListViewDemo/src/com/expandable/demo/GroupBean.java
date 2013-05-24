package com.expandable.demo;

import java.util.List;

/**
 *ExpandableList所用的group 
 *
 */
public class GroupBean {

	/**
	 * 标题
	 */
	private String title = "";
	
	/**
	 * child选择时提示的内容
	 */
	private String checkedInfo;
	
	/**
	 *相关联的childs 
	 */
	private List<ChildBean> childBeans;
	
	/**
	 * 相关联的childs被选中的个数
	 */
	public int checkedCount = 1;
	
	public List<ChildBean> getChildBeans() {
		return childBeans;
	}

	public void setChildBeans(List<ChildBean> childBeans) {
		this.childBeans = childBeans;
		
		/**
		 * 拼接checkedInfo字符串
		 */
		if(childBeans == null || childBeans.size() == 0 || childBeans.get(0).checked == true)
		{
			this.checkedInfo = "不限";
		}else
		{
			this.checkedInfo = "";
			//从第二项开始遍历
			for(int i = 1; i < childBeans.size(); i ++)
			{
				if(childBeans.get(i).checked)
				{
					String name = childBeans.get(i).name;
					this.checkedInfo = 
							this.checkedInfo.equals("")? name : this.checkedInfo + "," + name;
				}
			}
		}
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCheckedInfo() {
		return checkedInfo;
	}

}
