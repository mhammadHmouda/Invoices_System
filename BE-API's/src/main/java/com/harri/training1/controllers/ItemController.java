package com.harri.training1.controllers;

import com.harri.training1.models.entities.Item;
import com.harri.training1.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name){
        Item item = itemService.findByName(name);
        return ResponseEntity.ok(item);
    }

}
