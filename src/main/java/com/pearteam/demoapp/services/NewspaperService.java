package com.pearteam.demoapp.services;

import com.pearteam.demoapp.dao.Newspaper;
import com.pearteam.demoapp.dao.NewspaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewspaperService {

	@Autowired
	NewspaperRepository newspaperRepository;

	public List<Newspaper> getAll(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		Page<Newspaper> pagedResult = newspaperRepository.findAll(paging);

		if(pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Newspaper>();
		}
	}

	public List<Newspaper> findByName(Integer pageNo, Integer pageSize, String sortBy, String name) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		List<Newspaper> pagedResult = newspaperRepository.findAllByName(name, paging);

		return pagedResult;
	}



	public Page<Newspaper> findAllWithFilters(Integer pageNo, Integer pageSize, String sortBy, String name,
											  Integer width, Integer height, Integer dpi, String fileName) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		Page<Newspaper> pagedResult = newspaperRepository.findAllByNameAndWidthAndHeightAndDpiAndFileName(name, width, height, dpi, fileName, paging);
		return pagedResult;
	}


	public Iterable<Newspaper> getAll() {
		return newspaperRepository.findAll();
	}

	public long getCount() {
		return newspaperRepository.count();
	}
}
