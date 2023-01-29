package iae.post.naming;

import iae.post.poST.Function;
import iae.post.poST.FunctionBlock;
import iae.post.poST.InputOutputVarDeclaration;
import iae.post.poST.InputVarDeclaration;
import iae.post.poST.OutputVarDeclaration;
import iae.post.poST.ProcessVarDeclaration;
import iae.post.poST.ProcessVarInitDeclaration;
import iae.post.poST.ProcessVariable;
import iae.post.poST.Program;
import iae.post.poST.ProgramConfiguration;
import iae.post.poST.Resource;
import iae.post.poST.StatementList;
import iae.post.poST.SymbolicVariable;
import iae.post.poST.Task;
import iae.post.poST.TempVarDeclaration;
import iae.post.poST.TemplateProcessConfElement;
import iae.post.poST.VarDeclaration;
import iae.post.poST.VarInitDeclaration;
import iae.post.poST.Variable;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

@SuppressWarnings("all")
public class PoSTQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {
  protected QualifiedName qualifiedName(final Object ele) {
    boolean _matched = false;
    if (ele instanceof SymbolicVariable) {
      _matched=true;
      return this.getSymbolicVariableQualifiedName(((SymbolicVariable)ele));
    }
    if (!_matched) {
      if (ele instanceof TemplateProcessConfElement) {
        _matched=true;
        return this.getTemplateProcessConfElementQualifiedName(((TemplateProcessConfElement)ele));
      }
    }
    if (!_matched) {
      if (ele instanceof Task) {
        _matched=true;
        return this.getTaskQualifiedName(((Task)ele));
      }
    }
    if (!_matched) {
      if (ele instanceof Program) {
        _matched=true;
        return QualifiedName.create(((Program)ele).getName());
      }
    }
    if (!_matched) {
      if (ele instanceof FunctionBlock) {
        _matched=true;
        return QualifiedName.create(((FunctionBlock)ele).getName());
      }
    }
    if (!_matched) {
      if (ele instanceof Function) {
        _matched=true;
        return QualifiedName.create(((Function)ele).getName());
      }
    }
    return super.qualifiedName(ele);
  }
  
  private QualifiedName getSymbolicVariableQualifiedName(final SymbolicVariable ele) {
    boolean _checkVarInitDeclaration = this.checkVarInitDeclaration(ele);
    if (_checkVarInitDeclaration) {
      return this.getInitVariableQualifiedName(ele);
    }
    boolean _checkStatementList = this.checkStatementList(ele);
    if (_checkStatementList) {
      return this.getVariableQualifiedName(ele);
    }
    return QualifiedName.create(ele.getName());
  }
  
  private QualifiedName getTemplateProcessConfElementQualifiedName(final TemplateProcessConfElement ele) {
    final Resource res = EcoreUtil2.<Resource>getContainerOfType(ele, Resource.class);
    final ProgramConfiguration programConf = EcoreUtil2.<ProgramConfiguration>getContainerOfType(ele, ProgramConfiguration.class);
    return QualifiedName.create(res.getName(), programConf.getName(), ele.getName());
  }
  
  private QualifiedName getTaskQualifiedName(final Task ele) {
    final Resource res = EcoreUtil2.<Resource>getContainerOfType(ele, Resource.class);
    return QualifiedName.create(res.getName(), ele.getName());
  }
  
  private boolean checkVarInitDeclaration(final EObject ele) {
    return ((EcoreUtil2.<VarInitDeclaration>getContainerOfType(ele, VarInitDeclaration.class) != null) || (EcoreUtil2.<ProcessVarInitDeclaration>getContainerOfType(ele, ProcessVarInitDeclaration.class) != null));
  }
  
  private boolean checkStatementList(final EObject ele) {
    StatementList _containerOfType = EcoreUtil2.<StatementList>getContainerOfType(ele, StatementList.class);
    return (_containerOfType != null);
  }
  
  private QualifiedName getInitVariableQualifiedName(final SymbolicVariable ele) {
    final Program program = EcoreUtil2.<Program>getContainerOfType(ele, Program.class);
    if ((program != null)) {
      final iae.post.poST.Process process = EcoreUtil2.<iae.post.poST.Process>getContainerOfType(ele, iae.post.poST.Process.class);
      if ((process != null)) {
        return QualifiedName.create(program.getName(), process.getName(), ele.getName());
      }
      return QualifiedName.create(program.getName(), ele.getName());
    }
    return QualifiedName.create(ele.getName());
  }
  
  private QualifiedName getVariableQualifiedName(final SymbolicVariable ele) {
    final Program program = EcoreUtil2.<Program>getContainerOfType(ele, Program.class);
    if ((program != null)) {
      final iae.post.poST.Process process = EcoreUtil2.<iae.post.poST.Process>getContainerOfType(ele, iae.post.poST.Process.class);
      if (((process != null) && PoSTQualifiedNameProvider.checkProcessVars(process, ele.getName()))) {
        return QualifiedName.create(program.getName(), process.getName(), ele.getName());
      }
      boolean _checkProgramVars = PoSTQualifiedNameProvider.checkProgramVars(program, ele.getName());
      if (_checkProgramVars) {
        return QualifiedName.create(program.getName(), ele.getName());
      }
    }
    return QualifiedName.create(ele.getName());
  }
  
  public static boolean checkProgramVars(final Program program, final String eleName) {
    return (Stream.<EList<SymbolicVariable>>concat(
      program.getProgVars().stream().<EList<VarInitDeclaration>>map(new java.util.function.Function<VarDeclaration, EList<VarInitDeclaration>>() {
        public EList<VarInitDeclaration> apply(final VarDeclaration x) {
          return x.getVars();
        }
      }).<VarInitDeclaration>flatMap(new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
        public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
          return x.stream();
        }
      }).<EList<SymbolicVariable>>map(new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
        public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
          return x.getVarList().getVars();
        }
      }), 
      program.getProgTempVars().stream().<EList<VarInitDeclaration>>map(new java.util.function.Function<TempVarDeclaration, EList<VarInitDeclaration>>() {
        public EList<VarInitDeclaration> apply(final TempVarDeclaration x) {
          return x.getVars();
        }
      }).<VarInitDeclaration>flatMap(new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
        public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
          return x.stream();
        }
      }).<EList<SymbolicVariable>>map(new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
        public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
          return x.getVarList().getVars();
        }
      })).<SymbolicVariable>flatMap(new java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>>() {
      public Stream<SymbolicVariable> apply(final EList<SymbolicVariable> x) {
        return x.stream();
      }
    }).anyMatch(new Predicate<SymbolicVariable>() {
      public boolean test(final SymbolicVariable x) {
        return x.getName().equals(eleName);
      }
    }) || PoSTQualifiedNameProvider.checkProgramInOutVars(program, eleName));
  }
  
  public static boolean checkProgramInOutVars(final Program program, final String eleName) {
    final java.util.function.Function<InputVarDeclaration, EList<VarInitDeclaration>> _function = new java.util.function.Function<InputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final InputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_1 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_2 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<OutputVarDeclaration, EList<VarInitDeclaration>> _function_3 = new java.util.function.Function<OutputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final OutputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_4 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_5 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<InputOutputVarDeclaration, EList<VarInitDeclaration>> _function_6 = new java.util.function.Function<InputOutputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final InputOutputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_7 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_8 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>> _function_9 = new java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>>() {
      public Stream<SymbolicVariable> apply(final EList<SymbolicVariable> x) {
        return x.stream();
      }
    };
    final Predicate<SymbolicVariable> _function_10 = new Predicate<SymbolicVariable>() {
      public boolean test(final SymbolicVariable x) {
        return x.getName().equals(eleName);
      }
    };
    return Stream.<EList<SymbolicVariable>>concat(
      program.getProgInVars().stream().<EList<VarInitDeclaration>>map(_function).<VarInitDeclaration>flatMap(_function_1).<EList<SymbolicVariable>>map(_function_2), 
      Stream.<EList<SymbolicVariable>>concat(
        program.getProgOutVars().stream().<EList<VarInitDeclaration>>map(_function_3).<VarInitDeclaration>flatMap(_function_4).<EList<SymbolicVariable>>map(_function_5), 
        program.getProgInOutVars().stream().<EList<VarInitDeclaration>>map(_function_6).<VarInitDeclaration>flatMap(_function_7).<EList<SymbolicVariable>>map(_function_8))).<SymbolicVariable>flatMap(_function_9).anyMatch(_function_10);
  }
  
  public static boolean checkFBVars(final FunctionBlock fb, final String eleName) {
    return (Stream.<EList<SymbolicVariable>>concat(
      fb.getFbVars().stream().<EList<VarInitDeclaration>>map(new java.util.function.Function<VarDeclaration, EList<VarInitDeclaration>>() {
        public EList<VarInitDeclaration> apply(final VarDeclaration x) {
          return x.getVars();
        }
      }).<VarInitDeclaration>flatMap(new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
        public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
          return x.stream();
        }
      }).<EList<SymbolicVariable>>map(new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
        public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
          return x.getVarList().getVars();
        }
      }), 
      fb.getFbTempVars().stream().<EList<VarInitDeclaration>>map(new java.util.function.Function<TempVarDeclaration, EList<VarInitDeclaration>>() {
        public EList<VarInitDeclaration> apply(final TempVarDeclaration x) {
          return x.getVars();
        }
      }).<VarInitDeclaration>flatMap(new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
        public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
          return x.stream();
        }
      }).<EList<SymbolicVariable>>map(new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
        public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
          return x.getVarList().getVars();
        }
      })).<SymbolicVariable>flatMap(new java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>>() {
      public Stream<SymbolicVariable> apply(final EList<SymbolicVariable> x) {
        return x.stream();
      }
    }).anyMatch(new Predicate<SymbolicVariable>() {
      public boolean test(final SymbolicVariable x) {
        return x.getName().equals(eleName);
      }
    }) || PoSTQualifiedNameProvider.checkFBInOutVars(fb, eleName));
  }
  
  public static boolean checkFBInOutVars(final FunctionBlock fb, final String eleName) {
    final java.util.function.Function<InputVarDeclaration, EList<VarInitDeclaration>> _function = new java.util.function.Function<InputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final InputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_1 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_2 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<OutputVarDeclaration, EList<VarInitDeclaration>> _function_3 = new java.util.function.Function<OutputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final OutputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_4 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_5 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<InputOutputVarDeclaration, EList<VarInitDeclaration>> _function_6 = new java.util.function.Function<InputOutputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final InputOutputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_7 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_8 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>> _function_9 = new java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>>() {
      public Stream<SymbolicVariable> apply(final EList<SymbolicVariable> x) {
        return x.stream();
      }
    };
    final Predicate<SymbolicVariable> _function_10 = new Predicate<SymbolicVariable>() {
      public boolean test(final SymbolicVariable x) {
        return x.getName().equals(eleName);
      }
    };
    return Stream.<EList<SymbolicVariable>>concat(
      fb.getFbInVars().stream().<EList<VarInitDeclaration>>map(_function).<VarInitDeclaration>flatMap(_function_1).<EList<SymbolicVariable>>map(_function_2), 
      Stream.<EList<SymbolicVariable>>concat(
        fb.getFbOutVars().stream().<EList<VarInitDeclaration>>map(_function_3).<VarInitDeclaration>flatMap(_function_4).<EList<SymbolicVariable>>map(_function_5), 
        fb.getFbInOutVars().stream().<EList<VarInitDeclaration>>map(_function_6).<VarInitDeclaration>flatMap(_function_7).<EList<SymbolicVariable>>map(_function_8))).<SymbolicVariable>flatMap(_function_9).anyMatch(_function_10);
  }
  
  public static boolean checkProcessVars(final iae.post.poST.Process process, final String eleName) {
    return (Stream.<EList<? extends Variable>>concat(
      process.getProcVars().stream().<EList<VarInitDeclaration>>map(new java.util.function.Function<VarDeclaration, EList<VarInitDeclaration>>() {
        public EList<VarInitDeclaration> apply(final VarDeclaration x) {
          return x.getVars();
        }
      }).<VarInitDeclaration>flatMap(new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
        public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
          return x.stream();
        }
      }).<EList<SymbolicVariable>>map(new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
        public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
          return x.getVarList().getVars();
        }
      }), 
      Stream.<EList<? extends Variable>>concat(
        process.getProcTempVars().stream().<EList<VarInitDeclaration>>map(new java.util.function.Function<TempVarDeclaration, EList<VarInitDeclaration>>() {
          public EList<VarInitDeclaration> apply(final TempVarDeclaration x) {
            return x.getVars();
          }
        }).<VarInitDeclaration>flatMap(new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
          public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
            return x.stream();
          }
        }).<EList<SymbolicVariable>>map(new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
          public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
            return x.getVarList().getVars();
          }
        }), 
        process.getProcProcessVars().stream().<EList<ProcessVarInitDeclaration>>map(new java.util.function.Function<ProcessVarDeclaration, EList<ProcessVarInitDeclaration>>() {
          public EList<ProcessVarInitDeclaration> apply(final ProcessVarDeclaration x) {
            return x.getVars();
          }
        }).<ProcessVarInitDeclaration>flatMap(new java.util.function.Function<EList<ProcessVarInitDeclaration>, Stream<ProcessVarInitDeclaration>>() {
          public Stream<ProcessVarInitDeclaration> apply(final EList<ProcessVarInitDeclaration> x) {
            return x.stream();
          }
        }).<EList<ProcessVariable>>map(new java.util.function.Function<ProcessVarInitDeclaration, EList<ProcessVariable>>() {
          public EList<ProcessVariable> apply(final ProcessVarInitDeclaration x) {
            return x.getVarList().getVars();
          }
        }))).<Variable>flatMap(new java.util.function.Function<EList<? extends Variable>, Stream<? extends Variable>>() {
      public Stream<? extends Variable> apply(final EList<? extends Variable> x) {
        return x.stream();
      }
    }).anyMatch(new Predicate<Variable>() {
      public boolean test(final Variable x) {
        return x.getName().equals(eleName);
      }
    }) || PoSTQualifiedNameProvider.checkProcessInOutVars(process, eleName));
  }
  
  public static boolean checkProcessInOutVars(final iae.post.poST.Process process, final String eleName) {
    final java.util.function.Function<InputVarDeclaration, EList<VarInitDeclaration>> _function = new java.util.function.Function<InputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final InputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_1 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_2 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<OutputVarDeclaration, EList<VarInitDeclaration>> _function_3 = new java.util.function.Function<OutputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final OutputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_4 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_5 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<InputOutputVarDeclaration, EList<VarInitDeclaration>> _function_6 = new java.util.function.Function<InputOutputVarDeclaration, EList<VarInitDeclaration>>() {
      public EList<VarInitDeclaration> apply(final InputOutputVarDeclaration x) {
        return x.getVars();
      }
    };
    final java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>> _function_7 = new java.util.function.Function<EList<VarInitDeclaration>, Stream<VarInitDeclaration>>() {
      public Stream<VarInitDeclaration> apply(final EList<VarInitDeclaration> x) {
        return x.stream();
      }
    };
    final java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>> _function_8 = new java.util.function.Function<VarInitDeclaration, EList<SymbolicVariable>>() {
      public EList<SymbolicVariable> apply(final VarInitDeclaration x) {
        return x.getVarList().getVars();
      }
    };
    final java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>> _function_9 = new java.util.function.Function<EList<SymbolicVariable>, Stream<SymbolicVariable>>() {
      public Stream<SymbolicVariable> apply(final EList<SymbolicVariable> x) {
        return x.stream();
      }
    };
    final Predicate<SymbolicVariable> _function_10 = new Predicate<SymbolicVariable>() {
      public boolean test(final SymbolicVariable x) {
        return x.getName().equals(eleName);
      }
    };
    return Stream.<EList<SymbolicVariable>>concat(
      process.getProcInVars().stream().<EList<VarInitDeclaration>>map(_function).<VarInitDeclaration>flatMap(_function_1).<EList<SymbolicVariable>>map(_function_2), 
      Stream.<EList<SymbolicVariable>>concat(
        process.getProcOutVars().stream().<EList<VarInitDeclaration>>map(_function_3).<VarInitDeclaration>flatMap(_function_4).<EList<SymbolicVariable>>map(_function_5), 
        process.getProcInOutVars().stream().<EList<VarInitDeclaration>>map(_function_6).<VarInitDeclaration>flatMap(_function_7).<EList<SymbolicVariable>>map(_function_8))).<SymbolicVariable>flatMap(_function_9).anyMatch(_function_10);
  }
}
