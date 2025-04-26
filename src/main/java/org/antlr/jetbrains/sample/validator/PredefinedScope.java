package org.antlr.jetbrains.sample.validator;

import com.intellij.psi.PsiManager;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.jetbrains.annotations.NotNull;

public class PredefinedScope {
    public static boolean processDeclarations(PsiScopeProcessor processor,
                                              PsiManager manager) {
        // Process variables
        for (String varName : PredefinedSymbols.VARIABLES.keySet()) {
            PredefinedSymbols.VariableDescriptor descriptor = PredefinedSymbols.VARIABLES.get(varName);
            if (!processor.execute(
                    new PredefinedSymbolElement(manager, varName, descriptor.defaultValue),
                    ResolveState.initial()
            )) {
                return false;
            }
        }

        // Process functions
        //for (String funcName : PredefinedSymbols.FUNCTIONS) {
        //    if (!processor.execute(
        //            new PredefinedSymbolElement(manager, funcName),
        //            ResolveState.initial()
        //    )) {
        //        return false;
        //    }
        //}
        return true;
    }
}
