package org.eclipse.ice.developer.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class GitCloneHandler extends AbstractHandler {

	private String repo;
	
	public GitCloneHandler(String repoURL) {
		repo = repoURL;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

}
