/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST.impl;

import iae.post.poST.ExternalVarInitDeclaration;
import iae.post.poST.PoSTPackage;
import iae.post.poST.VarList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>External Var Init Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.impl.ExternalVarInitDeclarationImpl#getVarList <em>Var List</em>}</li>
 *   <li>{@link iae.post.poST.impl.ExternalVarInitDeclarationImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExternalVarInitDeclarationImpl extends MinimalEObjectImpl.Container implements ExternalVarInitDeclaration
{
  /**
   * The cached value of the '{@link #getVarList() <em>Var List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarList()
   * @generated
   * @ordered
   */
  protected VarList varList;

  /**
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final String TYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected String type = TYPE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ExternalVarInitDeclarationImpl()
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
    return PoSTPackage.Literals.EXTERNAL_VAR_INIT_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarList getVarList()
  {
    return varList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVarList(VarList newVarList, NotificationChain msgs)
  {
    VarList oldVarList = varList;
    varList = newVarList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST, oldVarList, newVarList);
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
  public void setVarList(VarList newVarList)
  {
    if (newVarList != varList)
    {
      NotificationChain msgs = null;
      if (varList != null)
        msgs = ((InternalEObject)varList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST, null, msgs);
      if (newVarList != null)
        msgs = ((InternalEObject)newVarList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST, null, msgs);
      msgs = basicSetVarList(newVarList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST, newVarList, newVarList));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setType(String newType)
  {
    String oldType = type;
    type = newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__TYPE, oldType, type));
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
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST:
        return basicSetVarList(null, msgs);
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
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST:
        return getVarList();
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__TYPE:
        return getType();
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
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST:
        setVarList((VarList)newValue);
        return;
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__TYPE:
        setType((String)newValue);
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
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST:
        setVarList((VarList)null);
        return;
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__TYPE:
        setType(TYPE_EDEFAULT);
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
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__VAR_LIST:
        return varList != null;
      case PoSTPackage.EXTERNAL_VAR_INIT_DECLARATION__TYPE:
        return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (type: ");
    result.append(type);
    result.append(')');
    return result.toString();
  }

} //ExternalVarInitDeclarationImpl
