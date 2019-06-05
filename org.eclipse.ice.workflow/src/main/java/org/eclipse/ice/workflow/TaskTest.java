package org.eclipse.ice.workflow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.EnumSet;

import org.eclipse.ice.workflow.WorkflowEngine.Events;
import org.eclipse.ice.workflow.WorkflowEngine.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;

public class TaskTest {

	static String name = "ICEIII/ice/org.eclipse.ice.workflow/src/main/resources/test1.txt";
	static String newName = "ICEIII/ice/org.eclipse.ice.workflow/src/main/resources/test2.txt";

	static public StateMachine<WorkflowEngine.States, WorkflowEngine.Events> buildMachine()
			throws Exception {
		Builder<WorkflowEngine.States, WorkflowEngine.Events> builder = new StateMachineBuilder.Builder<>();

		builder.configureStates().withStates().initial(WorkflowEngine.States.INITIALIZED)
				.states(EnumSet.allOf(WorkflowEngine.States.class));

		builder.configureTransitions().withExternal().source(WorkflowEngine.States.INITIALIZED)
				.target(WorkflowEngine.States.EXECUTING)
				.event(WorkflowEngine.Events.PARAMETERS_RECEIVED).and().withExternal()
				.source(WorkflowEngine.States.EXECUTING).target(WorkflowEngine.States.FINISHED)
				.action(new Action<WorkflowEngine.States, WorkflowEngine.Events>() {

					@Override
					public void execute(StateContext<States, Events> context) {
						try {
							moveFile(name, newName);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).event(WorkflowEngine.Events.EXECUTION_COMPLETE);

		return builder.build();
	}

	public static void moveFile(final String filePath, final String newFilePath)
			throws IOException {
		
		String home = System.getProperty("user.home");
		String separator = String.valueOf(File.separatorChar);
		String homePath = home+separator;
		
		Path source = (new File(homePath + filePath)).toPath();
		Path target = (new File(homePath + newFilePath)).toPath();
		Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		return;
	}

	public static void main(String[] args) throws Exception {

		StateMachine<WorkflowEngine.States, WorkflowEngine.Events> taskProcessor = buildMachine();
		taskProcessor.start();
		taskProcessor.sendEvent(WorkflowEngine.Events.PARAMETERS_RECEIVED);
		taskProcessor.sendEvent(WorkflowEngine.Events.EXECUTION_COMPLETE);
		System.out.println(taskProcessor.getState());

		return;
	}

}
