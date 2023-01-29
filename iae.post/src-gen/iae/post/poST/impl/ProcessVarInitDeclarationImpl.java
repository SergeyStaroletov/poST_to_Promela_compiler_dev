/**
 * generated by Xtext 2.26.0
 */
package iae.post.poST.impl;

import iae.post.poST.PoSTPackage;
import iae.post.poST.ProcessVarInitDeclaration;
import iae.post.poST.ProcessVarList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Var Init Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link iae.post.poST.impl.ProcessVarInitDeclarationImpl#getVarList <em>Var List</em>}</li>
 *   <li>{@link iae.post.poST.impl.ProcessVarInitDeclarationImpl#getProcess <em>Process</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProcessVarInitDeclarationImpl extends MinimalEObjectImpl.Container implements ProcessVarInitDeclaration
{
  /**
   * The cached value of the '{@link #getVarList() <em>Var List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarList()
   * @generated
   * @ordered
   */
  protected ProcessVarList varList;

  /**
   * The cached value of the '{@link #getProcess() <em>Process</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProcess()
   * @generated
   * @ordered
   */
  protected iae.post.poST.Process process;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProcessVarInitDeclarationImpl()
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
    return PoSTPackage.Literals.PROCESS_VAR_INIT_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProcessVarList getVarList()
  {
    return varList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVarList(ProcessVarList newVarList, NotificationChain msgs)
  {
    ProcessVarList oldVarList = varList;
    varList = newVarList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST, oldVarList, newVarList);
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
  public void setVarList(ProcessVarList newVarList)
  {
    if (newVarList != varList)
    {
      NotificationChain msgs = null;
      if (varList != null)
        msgs = ((InternalEObject)varList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST, null, msgs);
      if (newVarList != null)
        msgs = ((InternalEObject)newVarList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST, null, msgs);
      msgs = basicSetVarList(newVarList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST, newVarList, newVarList));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public iae.post.poST.Process getProcess()
  {
    if (process != null && process.eIsProxy())
    {
      InternalEObject oldProcess = (InternalEObject)process;
      process = (iae.post.poST.Process)eResolveProxy(oldProcess);
      if (process != oldProcess)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, PoSTPackage.PROCESS_VAR_INIT_DECLARATION__PROCESS, oldProcess, process));
      }
    }
    return process;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public iae.post.poST.Process basicGetProcess()
  {
    return process;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setProcess(iae.post.poST.Process newProcess)
  {
    iae.post.poST.Process oldProcess = process;
    process = newProcess;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, PoSTPackage.PROCESS_VAR_INIT_DECLARATION__PROCESS, oldProcess, process));
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
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST:
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
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST:
        return getVarList();
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__PROCESS:
        if (resolve) return getProcess();
        return basicGetProcess();
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
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST:
        setVarList((ProcessVarList)newValue);
        return;
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__PROCESS:
        setProcess((iae.post.poST.Process)newValue);
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
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST:
        setVarList((ProcessVarList)null);
        return;
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__PROCESS:
        setProcess((iae.post.poST.Process)null);
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
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__VAR_LIST:
        return varList != null;
      case PoSTPackage.PROCESS_VAR_INIT_DECLARATION__PROCESS:
        return process != null;
    }
    return super.eIsSet(featureID);
  }

} //ProcessVarInitDeclarationImpl