package org.eclipse.ice.client.widgets.moose;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.model.ICContainer;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class NewKernelHandler extends AbstractHandler {

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		TreeSelection selection = (TreeSelection) HandlerUtil
				.getCurrentSelection(event);
		Object segment = selection.getPaths()[0].getLastSegment();
		if (segment instanceof IProject) {
			IProject project = (IProject) segment;
		} else if (segment instanceof ICContainer) {
			ICContainer folder = (ICContainer) segment;
		}
		IParserLogService log = new DefaultLogService();

		String code = "class Class { public: int x,y; Class(); ~Class(); private: Class f(); }; int function(double parameter) { return parameter; };";
		FileContent reader = FileContent.create("kernel.hpp",code.toCharArray());
		Map definedSymbols = new HashMap();
		String[] includePaths = new String[0];
		IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);

		IASTTranslationUnit translationUnit = null;
		try {
			translationUnit = GCCLanguage.getDefault().getASTTranslationUnit(reader, info, null, null, 0,
					 log);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ASTVisitor visitor = new ASTVisitor() {
			public int visit(IASTName name) {
				System.out.println(name.getRawSignature());
				return ASTVisitor.PROCESS_CONTINUE;
			}
		};
		visitor.shouldVisitNames = true;
		translationUnit.accept(visitor);

		System.out.println("HELLO WORLD FROM NEW KERNEL HANDLER "
				+ selection.toString());
		return null;
	}

}
