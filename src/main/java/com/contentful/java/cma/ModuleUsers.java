/*
 * Copyright (C) 2017 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAUser;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Users Module.
 */
public final class ModuleUsers extends AbsModule<ServiceUsers> {
  final Async async;

  public ModuleUsers(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceUsers createService(Retrofit retrofit) {
    return retrofit.create(ServiceUsers.class);
  }

  /**
   * Fetch your user information.
   *
   * @return {@link CMAUser} result instance
   */
  public CMAUser fetchMe() {
    return service.fetchMe().toBlocking().first();
  }

  /**
   * @return a module with a set of asynchronous methods.
   */
  public Async async() {
    return async;
  }

  /**
   * Async module.
   */
  public final class Async {
    /**
     * Fetch your user information.
     *
     * @return {@link CMAUser} result instance
     */
    public CMACallback<CMAUser> fetchMe(CMACallback<CMAUser> callback) {
      return defer(new DefFunc<CMAUser>() {
        @Override CMAUser method() {
          return ModuleUsers.this.fetchMe();
        }
      }, callback);
    }
  }
}
