/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Interval</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.ArrayInterval#getStart <em>Start</em>}</li>
 *   <li>{@link iae.post.poST.ArrayInterval#getEnd <em>End</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getArrayInterval()
 * @model
 * @generated
 */
public interface ArrayInterval extends EObject
{
  /**
   * Returns the value of the '<em><b>Start</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Start</em>' containment reference.
   * @see #setStart(Expression)
   * @see iae.post.poST.PoSTPackage#getArrayInterval_Start()
   * @model containment="true"
   * @generated
   */
  Expression getStart();

  /**
   * Sets the value of the '{@link iae.post.poST.ArrayInterval#getStart <em>Start</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Start</em>' containment reference.
   * @see #getStart()
   * @generated
   */
  void setStart(Expression value);

  /**
   * Returns the value of the '<em><b>End</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>End</em>' containment reference.
   * @see #setEnd(Expression)
   * @see iae.post.poST.PoSTPackage#getArrayInterval_End()
   * @model containment="true"
   * @generated
   */
  Expression getEnd();

  /**
   * Sets the value of the '{@link iae.post.poST.ArrayInterval#getEnd <em>End</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>End</em>' containment reference.
   * @see #getEnd()
   * @generated
   */
  void setEnd(Expression value);

} // ArrayInterval
