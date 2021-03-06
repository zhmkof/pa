package cn.fyg.pa.interfaces.module.admin.deptkpi.departmentkpi;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.fyg.pa.application.DeptKpiService;
import cn.fyg.pa.application.IdrYearCompanyService;
import cn.fyg.pa.domain.model.companykpi.IdrYearCompany;
import cn.fyg.pa.domain.model.companykpiitem.IdrCompany;
import cn.fyg.pa.domain.model.companykpiitem.IdrCompanyRepository;
import cn.fyg.pa.domain.model.department.Department;
import cn.fyg.pa.domain.model.department.DepartmentRepository;
import cn.fyg.pa.domain.model.deptindicator.DeptIndicator;
import cn.fyg.pa.domain.model.deptindicator.DeptIndicatorRepository;
import cn.fyg.pa.domain.model.deptkpi.DeptKpi;
import cn.fyg.pa.domain.model.deptkpiitem.DeptKpiItem;
import cn.fyg.pa.interfaces.module.admin.deptkpi.departmentkpi.dto.list.ListPage;

@Component
public class DeptKpiFacade {
	
	@Resource
	IdrYearCompanyService idrYearCompanyService;
	@Resource
	DeptKpiService deptKpiService;
	@Resource
	DepartmentRepository departmentRepository;
	@Resource 
	IdrCompanyRepository idrCompanyRepository;
	@Resource
	DeptIndicatorRepository deptIndicatorRepository;
	
	
	public ListPage getDeptKpiByYearAndDepartment(Long year, Long departmentId) {
		IdrYearCompany idrYearCompany = idrYearCompanyService.findByYear(year);
		List<IdrCompany> idrCompanys = idrYearCompany.getIdrCompany();
		Department department = departmentRepository.find(departmentId);
		DeptKpi deptKpi = deptKpiService.getDeptKpiByYearAndDepartment(year, department);
		List<DeptKpiItem> deptKpiItems = deptKpi.getDeptKpiItems();
		DeptIndicator deptIndicator = deptIndicatorRepository.findByYearAndDepartment(year, department);
		ListBuilder builder=new ListBuilder(idrCompanys,deptKpiItems,deptIndicator);
		return builder.build(year,department);
	}
	
}
