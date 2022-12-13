package com.ltp.globalsuperstore.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ltp.globalsuperstore.Constants;
import com.ltp.globalsuperstore.Item;
import com.ltp.globalsuperstore.repository.StoreRepository;

public class StoreService {
    
    StoreRepository storeRepository = new StoreRepository();
    String status = Constants.SUCCESS_STATUS;

    public void submitItem(Item item) {
        int index = getIndexFromId(item.getId());

        if (index == Constants.NOT_FOUND) {
            storeRepository.addItem(item);
            status = Constants.SUCCESS_STATUS;
        } else if (within5Days(item.getDate(), storeRepository.getItem(index).getDate())) {
            storeRepository.updateItem(index, item);
            status = Constants.SUCCESS_STATUS;
        } else {
            status = Constants.FAILED_STATUS;
        }
    }

    public Item getItemById(String id) {
        int index = getIndexFromId(id);
        return index == Constants.NOT_FOUND ? new Item() : storeRepository.getItem(index);
    }

    public List<Item> getItems() {
        return storeRepository.getItems();
    }

    public int getIndexFromId(String id) {
        for (int i = 0; i < storeRepository.getItems().size(); i++) {
            if (storeRepository.getItem(i).getId().equals(id)) return i;
        }
        return Constants.NOT_FOUND;
    }

    public String getStatus() {
        return status;
    }

    public boolean within5Days(Date newDate, Date oldDate) {
        long diff = Math.abs(newDate.getTime() - oldDate.getTime());
        return (int) (TimeUnit.MILLISECONDS.toDays(diff)) <= 5;
    }
}
