package com.application.searching.queryLayer;


import javax.persistence.criteria.Subquery;

import com.application.people.User;
import com.application.posts.Post;
import com.application.searching.criteriaLayer.RootHandler;

public class UserQuery extends Query<User>
{
	public UserQuery(RootHandler<User> handler) {
		super(handler);
	}
	
}
