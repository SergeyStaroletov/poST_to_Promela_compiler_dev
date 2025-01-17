/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constant</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.Constant#getNum <em>Num</em>}</li>
 *   <li>{@link iae.post.poST.Constant#getTime <em>Time</em>}</li>
 *   <li>{@link iae.post.poST.Constant#getOth <em>Oth</em>}</li>
 * </ul>
 *
 * @see iae.post.poST.PoSTPackage#getConstant()
 * @model
 * @generated
 */
public interface Constant extends EObject
{
  /**
   * Returns the value of the '<em><b>Num</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Num</em>' containment reference.
   * @see #setNum(NumericLiteral)
   * @see iae.post.poST.PoSTPackage#getConstant_Num()
   * @model containment="true"
   * @generated
   */
  NumericLiteral getNum();

  /**
   * Sets the value of the '{@link iae.post.poST.Constant#getNum <em>Num</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Num</em>' containment reference.
   * @see #getNum()
   * @generated
   */
  void setNum(NumericLiteral value);

  /**
   * Returns the value of the '<em><b>Time</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Time</em>' containment reference.
   * @see #setTime(TimeLiteral)
   * @see iae.post.poST.PoSTPackage#getConstant_Time()
   * @model containment="true"
   * @generated
   */
  TimeLiteral getTime();

  /**
   * Sets the value of the '{@link iae.post.poST.Constant#getTime <em>Time</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Time</em>' containment reference.
   * @see #getTime()
   * @generated
   */
  void setTime(TimeLiteral value);

  /**
   * Returns the value of the '<em><b>Oth</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Oth</em>' attribute.
   * @see #setOth(String)
   * @see iae.post.poST.PoSTPackage#getConstant_Oth()
   * @model
   * @generated
   */
  String getOth();

  /**
   * Sets the value of the '{@link iae.post.poST.Constant#getOth <em>Oth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Oth</em>' attribute.
   * @see #getOth()
   * @generated
   */
  void setOth(String value);

} // Constant
