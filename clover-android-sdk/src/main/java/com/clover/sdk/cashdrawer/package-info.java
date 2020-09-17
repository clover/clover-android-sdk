/*
 * Copyright (C) 2016 Clover Network, Inc.
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
/**
 * This package contains classes for opening cash drawers connected to Clover devices. For example,
 * <pre>
 * <code>
 * Set&lt;CashDrawer&gt; drawers = new CashDrawers(context).list(); // get the set of connected cash drawers
 * for (CashDrawer cd : drawers) {
 *   cd.pop; // pop each connected cash drawer
 * }
 * </code>
 * </pre>
 */
package com.clover.sdk.cashdrawer;
