package com.expandable.demo;

import java.util.List;

/**
 *ExpandableList���õ�group 
 *
 */
public class GroupBean {

	/**
	 * ����
	 */
	private String title = "";
	
	/**
	 * childѡ��ʱ��ʾ������
	 */
	private String checkedInfo;
	
	/**
	 *�������childs 
	 */
	private List<ChildBean> childBeans;
	
	/**
	 * �������childs��ѡ�еĸ���
	 */
	public int checkedCount = 1;
	
	public List<ChildBean> getChildBeans() {
		return childBeans;
	}

	public void setChildBeans(List<ChildBean> childBeans) {
		this.childBeans = childBeans;
		
		/**
		 * ƴ��checkedInfo�ַ���
		 */
		if(childBeans == null || childBeans.size() == 0 || childBeans.get(0).checked == true)
		{
			this.checkedInfo = "����";
		}else
		{
			this.checkedInfo = "";
			//�ӵڶ��ʼ����
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
