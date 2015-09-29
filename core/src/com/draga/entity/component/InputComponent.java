package com.draga.entity.component;

public abstract class InputComponent extends Component
{
    public abstract void update(float elapsed);
    
    public abstract void dispose();
}
