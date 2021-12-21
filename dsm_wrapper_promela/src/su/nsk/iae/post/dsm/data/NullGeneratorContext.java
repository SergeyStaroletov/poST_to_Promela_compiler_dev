package su.nsk.iae.post.dsm.data;

import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.util.CancelIndicator;

public class NullGeneratorContext implements IGeneratorContext {
    @Override
    public CancelIndicator getCancelIndicator() {
        return null;
    }
}
