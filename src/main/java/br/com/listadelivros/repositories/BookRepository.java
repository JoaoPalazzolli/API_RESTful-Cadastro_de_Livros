package br.com.listadelivros.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.listadelivros.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
    
}
