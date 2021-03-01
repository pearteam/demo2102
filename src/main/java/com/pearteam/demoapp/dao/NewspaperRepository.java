package com.pearteam.demoapp.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewspaperRepository extends PagingAndSortingRepository<Newspaper, Long> {
	@Query("SELECT n FROM Newspaper n WHERE (:name is null or n.name = :name)")
	List<Newspaper> findAllByName(@Param("name") String name, Pageable paging);

	@Query("SELECT n FROM Newspaper n WHERE (:name is null OR n.name = :name)"
			+ " AND (:width is null OR n.width = :width)"
			+ " AND (:height is null OR n.height = :height)"
			+ " AND (:dpi is null OR n.dpi = :dpi)"
			+ " AND (:fileName is null OR n.fileName = :fileName)")
	Page<Newspaper> findAllByNameAndWidthAndHeightAndDpiAndFileName(
			@Param("name") String name,
			@Param("width") Integer width,
			@Param("height") Integer height,
			@Param("dpi") Integer dpi,
			@Param("fileName") String fileName,
			Pageable paging);
}
