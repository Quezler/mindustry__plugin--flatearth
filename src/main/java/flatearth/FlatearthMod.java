package flatearth;

import arc.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.plugin.*;
import mindustry.game.EventType.*;
import mindustry.core.GameState.*;

import static mindustry.Vars.*;

public class FlatearthMod extends Plugin{

    private boolean increment = false;
    private float alpha = 1f;
    private float net = alpha;

    // alpha pong's back and forth between +nighttime and -daytime

    private final float daytime = 1.5f;
    private final float nighttime = 1.5f;
    private final float rotational = 0.0005f; // haha, take that!

    @Override
    public void init(){
        Events.on(Trigger.update, () -> {
            if(!state.is(State.playing)) return;

            alpha = Mathf.clamp(alpha + (increment ? rotational : -rotational), -daytime, nighttime);

            if(alpha == -daytime) increment = true;
            if(alpha == nighttime) increment = false;

            state.rules.lighting = true;
            state.rules.ambientLight.a = Mathf.clamp(alpha, 0f, 1f); // clamp isn't "needed", but its cleaner.

            if(state.rules.ambientLight.a == net) return; // avoid needless calls, could probably be improved.

            net = state.rules.ambientLight.a;
            Call.onSetRules(state.rules);
        });
    }
}
