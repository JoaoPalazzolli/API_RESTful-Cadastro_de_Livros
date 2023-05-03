package br.com.listadelivros.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import br.com.listadelivros.controller.BookController;
import br.com.listadelivros.data.vo.v1.BookVO;
import br.com.listadelivros.exceptions.RequiredObjectIsNullException;
import br.com.listadelivros.exceptions.ResourceNotFoundException;
import br.com.listadelivros.mapper.DozerMapper;
import br.com.listadelivros.model.Book;
import br.com.listadelivros.repositories.BookRepository;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    private BookRepository repository;

    public List<BookVO> findAll() {
        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
        books.stream().forEach(x -> x.add(linkTo(methodOn(BookController.class).findById(x.getKey())).withSelfRel()));
        return books;
    }

    public BookVO findById(long id) {
        logger.info("Finding one Book!");

        var vo = DozerMapper.parseObject(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {
        logger.info("Creating one book!");

        if (book == null) throw new RequiredObjectIsNullException();

        var entity = DozerMapper.parseObject(book, Book.class);

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        logger.info("Updating one Book!");

        if (book == null) throw new RequiredObjectIsNullException();

        var entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity = DozerMapper.parseObject(book, Book.class);

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(long id){
        logger.info("Deleting one Book!");

        var book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(book);
    }
}
