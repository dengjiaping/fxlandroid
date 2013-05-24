package com.expandable.demo;

/**
 *ExpandableList所用的child 
 *
 */
public class ChildBean {
	
	/**
	 * 复选框是否被选中
	 */
	public boolean checked = false;
	
	/**
	 * 所显示的内容
	 */
	public String name = "";
	

	public ChildBean() {
		super();
	}

	public ChildBean(boolean checked, String name) {
		super();
		this.checked = checked;
		this.name = name;
	}
	
	
	

}
