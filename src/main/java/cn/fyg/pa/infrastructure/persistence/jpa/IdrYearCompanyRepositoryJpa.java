package cn.fyg.pa.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cn.fyg.pa.domain.model.companykpi.IdrYearCompany;
import cn.fyg.pa.domain.model.companykpi.IdrYearCompanyRepository;
import cn.fyg.pa.domain.model.companykpiitem.IdrCompany;
import cn.fyg.pa.domain.model.yeartypeweight.IdrTypeWeight;


@Repository
public class IdrYearCompanyRepositoryJpa implements IdrYearCompanyRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public IdrYearCompany find(Long id) {
		return entityManager.find(IdrYearCompany.class, id);
	}
	
	@Override
	public IdrYearCompany save(IdrYearCompany idrYearCompany){
		for(IdrCompany idrCompany:idrYearCompany.getIdrCompany()){
			idrCompany.setIdrYearCompany(idrYearCompany);
			IdrTypeWeight dbIdrTypeWeight=entityManager.find(IdrTypeWeight.class, idrCompany.getIdrTypeWeight().getId());
			idrCompany.setIdrTypeWeight(dbIdrTypeWeight);
			calculateRealWeight(idrCompany);
		}
		IdrYearCompany oldIdrYear=entityManager.find(IdrYearCompany.class, idrYearCompany.getYear());
		if(oldIdrYear==null){
			return create(idrYearCompany);
		}
		return update(idrYearCompany,oldIdrYear);
	}

	private IdrYearCompany create(IdrYearCompany idrYearCompany) {
		entityManager.persist(idrYearCompany);
		return idrYearCompany;
	}
	
	private IdrYearCompany update(IdrYearCompany idrYearCompany,IdrYearCompany oldIdrYearCompany) {
		Set<Long> idrCompamyIdSet=new HashSet<Long>();
		for(IdrCompany idrCompany:idrYearCompany.getIdrCompany()){
			idrCompany.setIdrYearCompany(idrYearCompany);
			idrCompamyIdSet.add(idrCompany.getId());
		}
		for(IdrCompany idrCompany:oldIdrYearCompany.getIdrCompany()){
			if(!idrCompamyIdSet.contains(idrCompany.getId())){
				//XXX 删除关联数据
				entityManager.createNativeQuery("delete from deptindicator_indiactoroptions where idrcompany_id=:idrcompany_id")
					.setParameter("idrcompany_id", idrCompany.getId())
					.executeUpdate();
				entityManager.createNativeQuery("delete from deptkpiitem where idrcompany_id=:idrcompany_id")
					.setParameter("idrcompany_id", idrCompany.getId())
					.executeUpdate();
				entityManager.remove(idrCompany);
			}
		}
		return entityManager.merge(idrYearCompany);
	}

	
	private void calculateRealWeight(IdrCompany idrCompany) {
		if(idrCompany.getWeight()==null) {
			idrCompany.setRealWeight(null);
			return;
		}
		if(idrCompany.getIdrTypeWeight()==null) {
			idrCompany.setRealWeight(null);
			return;
		}
		idrCompany.setRealWeight(idrCompany.getWeight().multiply(idrCompany.getIdrTypeWeight().getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
	}


}
