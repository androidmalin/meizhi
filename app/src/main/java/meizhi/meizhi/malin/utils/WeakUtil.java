package meizhi.meizhi.malin.utils;

import junit.framework.Assert;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class WeakUtil<_Target extends WeakDead> implements WeakDead, TargetObject<_Target> {
    private List<WeakReference<_Target>> pLu = new ArrayList();
    private boolean pLv = true;

    public final synchronized void a(_Target _Target) {
        if (this.pLv) {
            this.pLu.add(new WeakReference(_Target));
        } else {
            Assert.assertNotNull(_Target);
            _Target.dead();
        }
    }

    public final synchronized void dead() {
        if (this.pLv) {
            for (WeakReference weakReference : this.pLu) {
                WeakDead aVar = (WeakDead) weakReference.get();
                if (aVar != null) {
                    aVar.dead();
                }
            }
            this.pLu.clear();
            this.pLv = false;
        }
    }
}
