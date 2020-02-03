package com.clover.loyalty;
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

import com.clover.sdk.v3.loyalty.LoyaltyDataConfig;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Defines the comparison between LoyaltyDataConfig objects.
 * Useful in TreeMap implementations, and other Collections.
 */
public class LoyaltyDataConfigComparator implements Comparator<LoyaltyDataConfig> {
  @Override
  public int compare(LoyaltyDataConfig o1, LoyaltyDataConfig o2) {
    int comparisonResult = o1.getType().compareTo(o2.getType());
    if (comparisonResult != 0) {
      return comparisonResult;
    }
    // The types match, we need to check the configurations
    if ((o1.getConfiguration() == null) && (o2.getConfiguration() == null)) {
      // Types match, both configurations are null
      return comparisonResult;
    }
    if (o1.getConfiguration() == null && o2.getConfiguration().size() > 0) {
      // o1 has a null configuration, but o2 does not.  If the o2 configuration is empty, then we consider it null.
      // If o2 is larger than 0 then o2 is greater than o1
      comparisonResult = -1;
      return comparisonResult;
    }
    if (o2.getConfiguration() == null && o1.getConfiguration().size() > 0) {
      // o2 has a null configuration, but o1 does not.  If the o1 configuration is empty, then we consider it null.
      // If o1 is larger than 0 then o1 is greater than o2
      comparisonResult = 1;
      return comparisonResult;
    }
    // Lastly, we look at the configurations.  We know that neither of them are null.
    // Get all the keys, non duplicated and sorted, from the maps.
    Set<String> allKeys = new TreeSet<>(o1.getConfiguration().keySet());
    allKeys.addAll(o2.getConfiguration().keySet());
    for (String key1 : allKeys) {
      String value2 = o2.getConfiguration().get(key1);
      String value1 = o1.getConfiguration().get(key1);
      // if the values are equal, then continue.  This could mean that a key
      // is present in one but not the other.
      if (value1 == null && value2 == null) {
        continue;
      }
      if (value1 == null) {
        // value1 is null, but value 2 is not, value2 came from the greater value.
        return -1;
      }
      if (value2 == null) {
        // value2 is null, but value 1 is not, value1 came from the greater value.
        return 1;
      }
      comparisonResult = (value1.compareTo(value2));
      if (comparisonResult != 0) {
        return comparisonResult;
      }
    }
    return comparisonResult;
  }

  @Override
  public boolean equals(Object obj) {
    return obj.getClass() == this.getClass();
  }
}
