/*
 * Copyright (C) 2013 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.sdk.v1.inventory;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.ServiceException;

import java.util.List;

public class InventoryConnector extends ServiceConnector<IInventoryService> {
  public InventoryConnector(Context context, Account account, OnServiceConnectedListener client) {
    super(context, account, client);
  }

  protected String getServiceIntentAction() {
    return InventoryIntent.ACTION_INVENTORY_SERVICE;
  }


  protected IInventoryService getServiceInterface(IBinder iBinder) {
    return IInventoryService.Stub.asInterface(iBinder);
  }

  public List<Item> getItems() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<Item>>() {
      public List<Item> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItems(status);
      }
    });
  }

  public void getItems(Callback<List<Item>> callback) {
    execute(new ServiceCallable<IInventoryService, List<Item>>() {
      public List<Item> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItems(status);
      }
    }, callback);
  }

  public List<Item> getItemsWithCategories() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<Item>>() {
      public List<Item> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItemsWithCategories(status);
      }
    });
  }

  public void getItemsWithCategories(Callback<List<Item>> callback) {
    execute(new ServiceCallable<IInventoryService, List<Item>>() {
      public List<Item> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItemsWithCategories(status);
      }
    }, callback);
  }

  public List<String> getItemIds() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<String>>() {
      public List<String> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItemIds(status);
      }
    });
  }

  public void getItemIds(Callback<List<String>> callback) {
    execute(new ServiceCallable<IInventoryService, List<String>>() {
      public List<String> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItemIds(status);
      }
    }, callback);
  }

  public Item getItem(final String itemId) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, Item>() {
      public Item call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItem(itemId, status);
      }
    });
  }

  public void getItem(final String itemId, Callback<Item> callback) {
    execute(new ServiceCallable<IInventoryService, Item>() {
      public Item call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItem(itemId, status);
      }
    }, callback);
  }

  public Item getItemWithCategories(final String itemId) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, Item>() {
      public Item call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItemWithCategories(itemId, status);
      }
    });
  }

  public void getItemWithCategories(final String itemId, Callback<Item> callback) {
    execute(new ServiceCallable<IInventoryService, Item>() {
      public Item call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getItemWithCategories(itemId, status);
      }
    }, callback);
  }

  public Item createItem(final Item item) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, Item>() {
      public Item call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.createItem(item, status);
      }
    });
  }

  public void createItem(final Item item, Callback<Item> callback) {
    execute(new ServiceCallable<IInventoryService, Item>() {
      public Item call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.createItem(item, status);
      }
    }, callback);
  }

  public void updateItem(final Item item) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.updateItem(item, status);
      }
    });
  }

  public void updateItem(final Item item, Callback<Void> callback) {
    execute(new ServiceCallable<IInventoryService, Void>() {
      public Void call(IInventoryService service, ResultStatus status) throws RemoteException {
        service.updateItem(item, status);
        return null;
      }
    }, callback);
  }

  public void deleteItem(final String itemId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.deleteItem(itemId, status);
      }
    });
  }

  public void deleteItem(final String itemId, Callback<Void> callback) {
    execute(new ServiceCallable<IInventoryService, Void>() {
      public Void call(IInventoryService service, ResultStatus status) throws RemoteException {
        service.deleteItem(itemId, status);
        return null;
      }
    }, callback);
  }

  public List<CategoryDescription> getCategories() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<CategoryDescription>>() {
      public List<CategoryDescription> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getCategories(status);
      }
    });
  }

  public void getCategories(Callback<List<CategoryDescription>> callback) {
    execute(new ServiceCallable<IInventoryService, List<CategoryDescription>>() {
      public List<CategoryDescription> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getCategories(status);
      }
    }, callback);
  }

  public CategoryDescription createCategory(final CategoryDescription category) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, CategoryDescription>() {
      public CategoryDescription call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.createCategory(category, status);
      }
    });
  }

  public void createCategory(final CategoryDescription category, Callback<CategoryDescription> callback) {
    execute(new ServiceCallable<IInventoryService, CategoryDescription>() {
      public CategoryDescription call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.createCategory(category, status);
      }
    }, callback);
  }

  public void updateCategory(final CategoryDescription category) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.updateCategory(category, status);
      }
    });
  }

  public void updateCategory(final CategoryDescription category, Callback<Void> callback) {
    execute(new ServiceCallable<IInventoryService, Void>() {
      public Void call(IInventoryService service, ResultStatus status) throws RemoteException {
        service.updateCategory(category, status);
        return null;
      }
    }, callback);
  }

  public void deleteCategory(final String categoryId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.deleteCategory(categoryId, status);
      }
    });
  }

  public void deleteCategory(final String categoryId, Callback<Void> callback) {
    execute(new ServiceCallable<IInventoryService, Void>() {
      public Void call(IInventoryService service, ResultStatus status) throws RemoteException {
        service.deleteCategory(categoryId, status);
        return null;
      }
    }, callback);
  }

  public void addItemToCategory(final String itemId, final String categoryId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.addItemToCategory(itemId, categoryId, status);
      }
    });
  }

  public void addItemToCategory(final String itemId, final String categoryId, Callback<Void> callback) {
    execute(new ServiceCallable<IInventoryService, Void>() {
      public Void call(IInventoryService service, ResultStatus status) throws RemoteException {
        service.addItemToCategory(itemId, categoryId, status);
        return null;
      }
    }, callback);
  }

  public void removeItemFromCategory(final String itemId, final String categoryId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.removeItemFromCategory(itemId, categoryId, status);
      }
    });
  }

  public void removeItemFromCategory(final String itemId, final String categoryId, Callback<Void> callback) {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.removeItemFromCategory(itemId, categoryId, status);
      }
    }, callback);
  }

  public void moveItemInCategoryLayout(final String itemId, final String categoryId, final int direction) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.moveItemInCategoryLayout(itemId, categoryId, direction, status);
      }
    });
  }

  public void moveItemInCategoryLayout(final String itemId, final String categoryId, final int direction, Callback<Void> callback) {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.moveItemInCategoryLayout(itemId, categoryId, direction, status);
      }
    }, callback);
  }

  public List<ModifierGroup> getModifierGroups() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<ModifierGroup>>() {
      public List<ModifierGroup> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getModifierGroups(status);
      }
    });
  }

  public void getModifierGroups(Callback<List<ModifierGroup>> callback) {
    execute(new ServiceCallable<IInventoryService, List<ModifierGroup>>() {
      public List<ModifierGroup> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getModifierGroups(status);
      }
    }, callback);
  }

  public ModifierGroup createModifierGroup(final ModifierGroup group) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, ModifierGroup>() {
      public ModifierGroup call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.createModifierGroup(group, status);
      }
    });
  }

  public void createModifierGroup(final ModifierGroup modifierGroup, Callback<ModifierGroup> callback) {
    execute(new ServiceCallable<IInventoryService, ModifierGroup>() {
      public ModifierGroup call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.createModifierGroup(modifierGroup, status);
      }
    }, callback);
  }

  public void updateModifierGroup(final ModifierGroup group) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.updateModifierGroup(group, status);
      }
    });
  }

  public void deleteModifierGroup(final String groupId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.deleteModifierGroup(groupId, status);
      }
    });
  }

  public List<Modifier> getModifiers(final String modifierGroupId) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<Modifier>>() {
      public List<Modifier> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getModifiers(modifierGroupId, status);
      }
    });
  }

  public Modifier createModifier(final String modifierGroupId, final Modifier modifier) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, Modifier>() {
      public Modifier call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.createModifier(modifierGroupId, modifier, status);
      }
    });
  }

  public void updateModifier(final Modifier modifier) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.updateModifier(modifier, status);
      }
    });
  }

  public void deleteModifier(final String modifierId) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.deleteModifier(modifierId, status);
      }
    });
  }

  public List<TaxRate> getTaxRatesForItem(final String itemId) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<TaxRate>>() {
      public List<TaxRate> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getTaxRatesForItem(itemId, status);
      }
    });
  }

  public void assignTaxRatesToItem(final String itemId, final List<String> taxRates) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.assignTaxRatesToItem(itemId, taxRates, status);
      }
    });
  }

  public void removeTaxRatesFromItem(final String itemId, final List<String> taxRates) throws ClientException, ServiceException, BindingException, RemoteException {
    execute(new ServiceRunnable<IInventoryService>() {
      public void run(IInventoryService service, ResultStatus status) throws RemoteException {
        service.removeTaxRatesFromItem(itemId, taxRates, status);
      }
    });
  }

  public List<TaxRate> getTaxRates() throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, List<TaxRate>>() {
      public List<TaxRate> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getTaxRates(status);
      }
    });
  }

  public void getTaxRates(Callback<List<TaxRate>> callback) {
    execute(new ServiceCallable<IInventoryService, List<TaxRate>>() {
      public List<TaxRate> call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getTaxRates(status);
      }
    }, callback);
  }

  public TaxRate getTaxRate(final String taxRateId) throws ClientException, ServiceException, BindingException, RemoteException {
    return execute(new ServiceCallable<IInventoryService, TaxRate>() {
      public TaxRate call(IInventoryService service, ResultStatus status) throws RemoteException {
        return service.getTaxRate(taxRateId, status);
      }
    });
  }
}
