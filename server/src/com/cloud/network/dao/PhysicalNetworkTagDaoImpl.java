/**
 *  Copyright (C) 2011 Citrix Systems, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.cloud.network.dao;

import java.util.List;

import com.cloud.utils.db.GenericDao;
import com.cloud.utils.db.GenericDaoBase;
import com.cloud.utils.db.GenericSearchBuilder;
import com.cloud.utils.db.SearchBuilder;
import com.cloud.utils.db.SearchCriteria;
import com.cloud.utils.db.SearchCriteria.Op;


public class PhysicalNetworkTagDaoImpl extends GenericDaoBase<PhysicalNetworkTagVO, Long> implements GenericDao<PhysicalNetworkTagVO, Long> {
    private final GenericSearchBuilder<PhysicalNetworkTagVO, String> TagSearch;
    private final SearchBuilder<PhysicalNetworkTagVO> AllFieldsSearch;

    protected PhysicalNetworkTagDaoImpl() {
        super();
        TagSearch = createSearchBuilder(String.class);
        TagSearch.selectField(TagSearch.entity().getTag());
        TagSearch.and("physicalNetworkId", TagSearch.entity().getPhysicalNetworkId(), Op.EQ);
        TagSearch.done();

        AllFieldsSearch = createSearchBuilder();
        AllFieldsSearch.and("id", AllFieldsSearch.entity().getId(), Op.EQ);
        AllFieldsSearch.and("physicalNetworkId", AllFieldsSearch.entity().getPhysicalNetworkId(), Op.EQ);
        AllFieldsSearch.and("tag", AllFieldsSearch.entity().getTag(), Op.EQ);
        AllFieldsSearch.done();
    }

    public List<String> getTags(long physicalNetworkId) {
        SearchCriteria<String> sc = TagSearch.create();
        sc.setParameters("physicalNetworkId", physicalNetworkId);

        return customSearch(sc, null);
    }

    public int clearTags(long physicalNetworkId) {
        SearchCriteria<PhysicalNetworkTagVO> sc = AllFieldsSearch.create();
        sc.setParameters("physicalNetworkId", physicalNetworkId);

        return remove(sc);
    }

}
