package com.application.searching.inputLayer;

import com.application.people.User;
import com.application.posts.Post;
import com.application.searching.QueryService;
import com.application.searching.criteriaLayer.RootHandler;
import com.application.searching.queryLayer.QueryPart;
import com.application.searching.queryLayer.UserQuery;

public class MappedUserQuery extends MappedQuery<User>
{
	private MappedUserPart base;
	

	@Override
	public UserQuery firstmap(QueryService service) {
		//TODO 
		return null;
	}


}
