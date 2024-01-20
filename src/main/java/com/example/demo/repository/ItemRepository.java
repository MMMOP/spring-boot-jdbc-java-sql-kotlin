package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {

  Set<Item> findByNoOrderByNameDesc(Integer no);

  List<Item> findByNameContaining(String name);

}