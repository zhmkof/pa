package cn.fyg.pa.interfaces.yearchk;

import java.util.ArrayList;
import java.util.List;

public class PageBeanBuilder {
	
	List<PersonChkBean> personChkBeans;

	public PageBeanBuilder(List<PersonChkBean> personChkBeans) {
		super();
		this.personChkBeans = personChkBeans;
	}
	
	//XXX 未完成，尝试计算pagebean
	public PageBean builder(Long year,int needChkPerson){
		
		PageBean pageBean=new PageBean();
		List<DepartmentChkBean> departmentChkBeanList = getDepartmentChkBeanList();
		pageBean.setYear(year);
		pageBean.setNeedChkPerson(needChkPerson);
		pageBean.setDepartmentChkBeans(departmentChkBeanList);
		pageBean.calculateSelf();
		
		return pageBean;
	}

	private List<DepartmentChkBean> getDepartmentChkBeanList() {
		List<DepartmentChkBean> departmentChkBeanList=new ArrayList<DepartmentChkBean>();
		
		DepartmentChkBean departmentChkBean = createDepartmentChkBean(this.personChkBeans.get(0));
		departmentChkBeanList.add(departmentChkBean);
		for(PersonChkBean personChkBean:this.personChkBeans){
			if(!departmentChkBean.getDepartment().equals(personChkBean.getDepartment())){
				departmentChkBean=createDepartmentChkBean(personChkBean);
				departmentChkBeanList.add(departmentChkBean);
			}
			departmentChkBean.getPersonChkBeans().add(personChkBean);
		}
		return departmentChkBeanList;
	}

	private DepartmentChkBean createDepartmentChkBean(PersonChkBean personChkBean) {
		DepartmentChkBean departmentChkBean=new DepartmentChkBean();
		departmentChkBean.setDepartment(personChkBean.getDepartment());
		return departmentChkBean;
	}

	
}
