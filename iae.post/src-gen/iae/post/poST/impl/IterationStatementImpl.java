/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST.impl;

import iae.post.poST.IterationStatement;
import iae.post.poST.PoSTPackage;
import iae.post.poST.StatementList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Iteration Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.impl.IterationStatementImpl#getStatement <em>Statement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IterationStatementImpl extends StatementImpl implements IterationStatement
{
  /**
   * The cached value of the '{@link #getStatement() <em>Statement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStatement()
   * @generated
   * @ordered
   */
  protected StatementList statement;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IterationStatementImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return PoSTPackage.Literals.ITERATION_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StatementList getStatement()
  {
    return statement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetStatement(StatementList newStatement, NotificationChain msgs)
  {
    StatementList oldStatement = statement;
    statement = newStatement;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PoSTPackage.ITERATION_STATEMENT__STATEMENT, oldStatement, newStatement);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setStatement(StatementList newStatement)
  {
    if (newStatement != statement)
    {
      NotificationChain msgs = null;
      if (statement != null)
        msgs = ((InternalEObject)statement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PoSTPackage.ITERATION_STATEMENT__STATEMENT, null, msgs);
      if (newStatement != null)
        msgs = ((InternalEObject)newStatement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PoSTPackage.ITERATION_STATEMENT__STATEMENT, null, msgs);
      msgs = basicSetStatement(newStatement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PoSTPackage.ITERATION_STATEMENT__STATEMENT, newStatement, newStatement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case PoSTPackage.ITERATION_STATEMENT__STATEMENT:
        return basicSetStatement(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case PoSTPackage.ITERATION_STATEMENT__STATEMENT:
        return getStatement();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case PoSTPackage.ITERATION_STATEMENT__STATEMENT:
        setStatement((StatementList)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case PoSTPackage.ITERATION_STATEMENT__STATEMENT:
        setStatement((StatementList)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case PoSTPackage.ITERATION_STATEMENT__STATEMENT:
        return statement != null;
    }
    return super.eIsSet(featureID);
  }

} //IterationStatementImpl
