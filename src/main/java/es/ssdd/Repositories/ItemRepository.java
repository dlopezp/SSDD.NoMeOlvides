package es.ssdd.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.ssdd.Models.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
