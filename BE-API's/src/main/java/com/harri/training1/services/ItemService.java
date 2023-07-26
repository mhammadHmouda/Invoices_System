package com.harri.training1.services;

import com.harri.training1.exceptions.InvoiceNotExistException;
import com.harri.training1.models.entities.Item;
import com.harri.training1.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Item findByName(String name){
        Item item = itemRepository.findByName(name);

        if(item == null)
            throw new InvoiceNotExistException("Item with name: " + name + " not exist!");

        return item;
    }
}
