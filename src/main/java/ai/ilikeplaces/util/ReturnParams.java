package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 13, 2010
 * Time: 10:11:46 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ReturnParams implements ReturnParamsFace {

    private List<Object> params;

    public ReturnParams(final Object... params) {
        final List<Object> localParams = new ArrayList<Object>();
        for (final Object param : params) {
            localParams.add(param);
        }
        this.params = localParams;
    }


    @Override
    public ReturnParamsFace add(final Object... obj) {
        params.add(obj);
        return this;
    }    

    @Override
    public ReturnParamsFace set(final int index, final Object... obj) {
        params.set(index, obj);
        return this;
    }

    @Override
    public Object get(final int index) {
        return params.get(index);
    }
}