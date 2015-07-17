///*******************************************************************************
// * Copyright (c) 2014 UT-Battelle, LLC.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
// *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
// *   Claire Saunders, Matthew Wang, Anna Wojtowicz
// *******************************************************************************/
//package org.eclipse.ice.viz;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import javafx.event.EventHandler;
//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.PerspectiveCamera;
//import javafx.scene.Scene;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Box;
//import javafx.scene.shape.Cylinder;
//import javafx.scene.shape.Sphere;
//import javafx.scene.transform.Rotate;
//
//import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
//import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
//import org.eclipse.ice.datastructures.form.ResourceComponent;
//import org.eclipse.ice.datastructures.resource.ICEResource;
//import org.eclipse.ice.datastructures.resource.VizResource;
//import org.eclipse.ice.viz.csv.viewer.CSVPlotViewer;
//import org.eclipse.ice.viz.visit.VisitPlotViewer;
//import org.eclipse.jface.action.IToolBarManager;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.ISelectionChangedListener;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.jface.viewers.ITreeContentProvider;
//import org.eclipse.jface.viewers.SelectionChangedEvent;
//import org.eclipse.jface.viewers.StructuredSelection;
//import org.eclipse.jface.viewers.StyledCellLabelProvider;
//import org.eclipse.jface.viewers.StyledString;
//import org.eclipse.jface.viewers.TreeViewer;
//import org.eclipse.jface.viewers.Viewer;
//import org.eclipse.jface.viewers.ViewerCell;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.ui.IActionBars;
//import org.eclipse.ui.IViewReference;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.part.ViewPart;
//
//import javafx.embed.swt.FXCanvas;
//import javafx.scene.Parent;
////import javafx.event.EventHandler;
//
//
//
///**
// * This class extends the ViewPart class and provides a view in the
// * Visualization Perspective to look at the files that are currently available
// * to use for creating plots.
// * 
// * @author Jay Jay Billings
// * @author Taylor Patterson
// * @author Jordan H. Deyton
// */
//public class JavaFXView extends ViewPart{
//	Parent dsfsdf;
//    final static Group root = new Group();
//    final static Xform axisGroup = new Xform();
//    final static Xform moleculeGroup = new Xform();
//    final static Xform world = new Xform();
//    final static PerspectiveCamera camera = new PerspectiveCamera(true);
//    final static Xform cameraXform = new Xform();
//    final static Xform cameraXform2 = new Xform();
//    final static Xform cameraXform3 = new Xform();
//    private static final double CAMERA_INITIAL_DISTANCE = -450;
//    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
//    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
//    private static final double CAMERA_NEAR_CLIP = 0.1;
//    private static final double CAMERA_FAR_CLIP = 10000.0;
//    private static final double AXIS_LENGTH = 250.0;
//    private static final double HYDROGEN_ANGLE = 104.5;
//    final static double CONTROL_MULTIPLIER = 0.1;    
//    final static double SHIFT_MULTIPLIER = 10.0;    
//    final static double MOUSE_SPEED = 0.1;    
//    final static double ROTATION_SPEED = 2.0;   
//    final static double TRACK_SPEED = 0.3;
//    
//    static double mousePosX;
//    static double mousePosY;
//    static double mouseOldX;
//    static double mouseOldY;
//    static double mouseDeltaX;
//    static double mouseDeltaY;
//    static int modifierFactor= 1;
//    
//    private static void buildCamera() {
//        root.getChildren().add(cameraXform);
//        cameraXform.getChildren().add(cameraXform2);
//        cameraXform2.getChildren().add(cameraXform3);
//        cameraXform3.getChildren().add(camera);
//        cameraXform3.setRotateZ(180.0);
// 
//        camera.setNearClip(CAMERA_NEAR_CLIP);
//        camera.setFarClip(CAMERA_FAR_CLIP);
//        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
//        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
//        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
//
//    }
//    
//    private static void buildAxes() {
//        final PhongMaterial redMaterial = new PhongMaterial();
//        redMaterial.setDiffuseColor(Color.DARKRED);
//        redMaterial.setSpecularColor(Color.RED);
// 
//        final PhongMaterial greenMaterial = new PhongMaterial();
//        greenMaterial.setDiffuseColor(Color.DARKGREEN);
//        greenMaterial.setSpecularColor(Color.GREEN);
// 
//        final PhongMaterial blueMaterial = new PhongMaterial();
//        blueMaterial.setDiffuseColor(Color.DARKBLUE);
//        blueMaterial.setSpecularColor(Color.BLUE);
// 
//        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
//        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
//        final Box zAxis = new Box(1, 1, AXIS_LENGTH);
//        
//        xAxis.setMaterial(redMaterial);
//        yAxis.setMaterial(greenMaterial);
//        zAxis.setMaterial(blueMaterial);
// 
//        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
//        axisGroup.setVisible(true);
//        world.getChildren().addAll(axisGroup);
//    }
//    
//    private static void buildMolecule() {
//    	 
//        final PhongMaterial redMaterial = new PhongMaterial();
//        redMaterial.setDiffuseColor(Color.DARKRED);
//        redMaterial.setSpecularColor(Color.RED);
//  
//        final PhongMaterial whiteMaterial = new PhongMaterial();
//        whiteMaterial.setDiffuseColor(Color.WHITE);
//        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);
//  
//        final PhongMaterial greyMaterial = new PhongMaterial();
//        greyMaterial.setDiffuseColor(Color.DARKGREY);
//        greyMaterial.setSpecularColor(Color.GREY);
//  
//        // Molecule Hierarchy
//        // [*] moleculeXform
//        //     [*] oxygenXform
//        //         [*] oxygenSphere
//        //     [*] hydrogen1SideXform
//        //         [*] hydrogen1Xform
//        //             [*] hydrogen1Sphere
//        //         [*] bond1Cylinder
//        //     [*] hydrogen2SideXform
//        //         [*] hydrogen2Xform
//        //             [*] hydrogen2Sphere
//        //         [*] bond2Cylinder
//  
//        Xform moleculeXform = new Xform();
//        Xform oxygenXform = new Xform();
//        Xform hydrogen1SideXform = new Xform();
//        Xform hydrogen1Xform = new Xform();
//        Xform hydrogen2SideXform = new Xform();
//        Xform hydrogen2Xform = new Xform();
//
//       Sphere oxygenSphere = new Sphere(40.0);
//       oxygenSphere.setMaterial(redMaterial);
//
//       Sphere hydrogen1Sphere = new Sphere(30.0);
//       hydrogen1Sphere.setMaterial(whiteMaterial);
//       hydrogen1Sphere.setTranslateX(0.0);
//
//       Sphere hydrogen2Sphere = new Sphere(30.0);
//       hydrogen2Sphere.setMaterial(whiteMaterial);
//       hydrogen2Sphere.setTranslateZ(0.0);
//
//       Cylinder bond1Cylinder = new Cylinder(5, 100);
//       bond1Cylinder.setMaterial(greyMaterial);
//       bond1Cylinder.setTranslateX(50.0);
//       bond1Cylinder.setRotationAxis(Rotate.Z_AXIS);
//       bond1Cylinder.setRotate(90.0);
//
//       Cylinder bond2Cylinder = new Cylinder(5, 100);
//       bond2Cylinder.setMaterial(greyMaterial);
//       bond2Cylinder.setTranslateX(50.0);
//       bond2Cylinder.setRotationAxis(Rotate.Z_AXIS);
//       bond2Cylinder.setRotate(90.0);
//
//       moleculeXform.getChildren().add(oxygenXform);
//       moleculeXform.getChildren().add(hydrogen1SideXform);
//       moleculeXform.getChildren().add(hydrogen2SideXform);
//       oxygenXform.getChildren().add(oxygenSphere);
//       hydrogen1SideXform.getChildren().add(hydrogen1Xform);
//       hydrogen2SideXform.getChildren().add(hydrogen2Xform);
//       hydrogen1Xform.getChildren().add(hydrogen1Sphere);
//       hydrogen2Xform.getChildren().add(hydrogen2Sphere);
//       hydrogen1SideXform.getChildren().add(bond1Cylinder);
//       hydrogen2SideXform.getChildren().add(bond2Cylinder);
//  
//       hydrogen1Xform.setTx(100.0);
//       hydrogen2Xform.setTx(100.0);
//       hydrogen2SideXform.setRotateY(HYDROGEN_ANGLE);
//       
//       moleculeGroup.getChildren().add(moleculeXform);
//
//       world.getChildren().addAll(moleculeGroup);
// }
//    
//    private static void handleMouse(Scene scene, final Node root) {
//    	 
//        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent me) {
//                mousePosX = me.getSceneX();
//                mousePosY = me.getSceneY();
//                mouseOldX = me.getSceneX();
//                mouseOldY = me.getSceneY();
//            }
//        });
//        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent me) {
//                mouseOldX = mousePosX;
//                mouseOldY = mousePosY;
//                mousePosX = me.getSceneX();
//                mousePosY = me.getSceneY();
//                mouseDeltaX = (mousePosX - mouseOldX); 
//                mouseDeltaY = (mousePosY - mouseOldY);
//
//               double modifier = 1.0;
//
//               if (me.isControlDown()) {
//                    modifier = CONTROL_MULTIPLIER;
//                } 
//                if (me.isShiftDown()) {
//                    modifier = SHIFT_MULTIPLIER;
//                }     
//                if (me.isPrimaryButtonDown()) {
//                	System.out.println(cameraXform.ry);
//                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
//                       mouseDeltaX*modifierFactor*modifier*ROTATION_SPEED);  // 
//                   cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
//                       mouseDeltaY*modifierFactor*modifier*ROTATION_SPEED);  // -
//                }
//                else if (me.isSecondaryButtonDown()) {
//                    double z = camera.getTranslateZ();
//                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
//                    camera.setTranslateZ(newZ);
//                }
//                else if (me.isMiddleButtonDown()) {
//                   cameraXform2.t.setX(cameraXform2.t.getX() + 
//                      mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
//                   cameraXform2.t.setY(cameraXform2.t.getY() + 
//                      mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
//                }
//           }
//       }); // setOnMouseDragged
//   } //handleMouse
//    
//    private static void handleKeyboard(Scene scene, final Node root) {
//
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//               switch (event.getCode()) {
//                   case Z:
//                       cameraXform2.t.setX(0.0);
//                       cameraXform2.t.setY(0.0);
//                       cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
//                       cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
//                       break;
//                   case X:
//                        axisGroup.setVisible(!axisGroup.isVisible());
//                        break;
//                    case V:
//                       moleculeGroup.setVisible(!moleculeGroup.isVisible());
//                       break;
//               } // switch
//            } // handle()
//        });  // setOnKeyPressed
//    }  //  handleKeyboard()
//
////    public static void main(String[] args) {    	
////    	axisGroup.setVisible(false);
////	buildCamera();
////	buildAxes();
////	buildMolecule();
////    	final Group root = new Group();
////        Display display = new Display();
////        Shell shell = new Shell(display);
////        shell.setLayout(new FillLayout());
////        FXCanvas canvas = new FXCanvas(shell, SWT.NONE);
////        final PhongMaterial blueMaterial = new PhongMaterial();
////        blueMaterial.setDiffuseColor(Color.BLUE);
////        blueMaterial.setSpecularColor(Color.LIGHTBLUE);
////        
////        final Sphere blue = new Sphere(200);
////        blue.setMaterial(blueMaterial);
//// 
////        Scene scene = new Scene(root, 1024, 768, true);
////        canvas.setScene(scene);
////        scene.setFill(Color.GREY);
////        
////        handleKeyboard(scene, world);
////        handleMouse(scene, world);
////
////        
////        
////        root.getChildren().add(world);
////        
////
////        scene.setCamera(camera);      
////        
////
////        shell.open();
////  
////        
////        while (!shell.isDisposed()) {
////            if (!display.readAndDispatch()) display.sleep();
////        }
////        display.dispose();
////    }
//
//	/**
//	 * The ID for this view.
//	 */
//	public static final String ID = "org.eclipse.ice.viz.VizFileViewer";
//
//	/**
//	 * The active ResourceComponent.
//	 */
//	private ResourceComponent resourceComponent;
//
//	/**
//	 * A TreeViewer that shows imported files. This is created in
//	 * {@link #createPartControl(Composite)}.
//	 */
//	private TreeViewer fileTreeViewer;
//
//	/**
//	 * This action opens a dialog for selecting files to add to this viewer.
//	 */
//	private AddFileAction addFileAction;
//
//	/**
//	 * This action calls {@link #removeSelection()} to remove all selected
//	 * resources from this viewer.
//	 */
//	private DeleteFileAction deleteFileAction;
//
//	/**
//	 * The default constructor.
//	 */
//	public JavaFXView() {
//		// Set a default ResourceComponent.
//		//setResourceComponent(new ResourceComponent());
//	}
//
//	/**
//	 * Creates the widgets and controls for the VizFileViewer. This includes
//	 * {@link #fileTreeViewer}.
//	 * 
//	 * @param parent
//	 *            The parent Composite that will contain this VizFileViewer.
//	 */
//	@Override
//	public void createPartControl(Composite parent1) {
//
//       	axisGroup.setVisible(false);
//   	buildCamera();
//   	buildAxes();
//   	buildMolecule();
//       	final Group root = new Group();
//          // Display display = new Display();
//           //Shell shell = new Shell(display);
//           //shell.setLayout(new FillLayout());
//           FXCanvas canvas = new FXCanvas(parent1, SWT.NONE);
//           //final PhongMaterial blueMaterial = new PhongMaterial();
//           //blueMaterial.setDiffuseColor(Color.BLUE);
//           //blueMaterial.setSpecularColor(Color.LIGHTBLUE);
//           
//           //final Sphere blue = new Sphere(200);
//           //blue.setMaterial(blueMaterial);
//    
//           Scene scene = new Scene(root, 1024, 768, true);
//           canvas.setScene(scene);
//           scene.setFill(Color.GREY);
//           
//           handleKeyboard(scene, world);
//           handleMouse(scene, world);
//
//           
//           
//           root.getChildren().add(world);
//           
//
//           scene.setCamera(camera);      
//
//		return;
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//}
