/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.Task#getName <em>Name</em>}</li>
 *   <li>{@link iae.post.poST.Task#getInit <em>Init</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getTask()
 * @model
 * @generated
 */
public interface Task extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see iae.post.poST.PoSTPackage#getTask_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link iae.post.poST.Task#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Init</em>' containment reference.
   * @see #setInit(TaskInitialization)
   * @see iae.post.poST.PoSTPackage#getTask_Init()
   * @model containment="true"
   * @generated
   */
  TaskInitialization getInit();

  /**
   * Sets the value of the '{@link iae.post.poST.Task#getInit <em>Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Init</em>' containment reference.
   * @see #getInit()
   * @generated
   */
  void setInit(TaskInitialization value);

} // Task
