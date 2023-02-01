/*
 * MIT License
 *
 * Copyright (c) 2022 Ciprian Teodorov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package obp2.language.state_event.runtime;

import obp2.language.state_event.model.StateEventTransition;
import obp2.runtime.core.IMarshaller;
import obp2.runtime.core.LanguageModule;
import obp2.runtime.core.defaults.DefaultLanguageService;
import plug.utils.marshaling.Marshaller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StateEventMarshaller
        extends DefaultLanguageService<StateEventConfiguration, StateEventTransition, byte[]>
        implements IMarshaller<StateEventConfiguration, StateEventTransition, byte[]> {

    public StateEventMarshaller(LanguageModule remoteLanguageModule) {
        super();
        this.setModule(remoteLanguageModule);
    }

    @Override
    public byte[] serializeConfiguration(StateEventConfiguration configuration) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Marshaller.writeInt(configuration.id, baos);
            Marshaller.writeIntArray(configuration.values, baos);

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] serializeFireable(StateEventTransition fireable) {
        return null;
    }

    @Override
    public byte[] serializeOutput(byte[] output) {
        return output;
    }

    @Override
    public StateEventConfiguration deserializeConfiguration(byte[] buffer) {
        return null;
    }

    @Override
    public StateEventTransition deserializeFireable(byte[] buffer) {
        return null;
        //new StateEventTransition(buffer);
    }

    @Override
    public byte[] deserializeOutput(byte[] buffer) {
        return buffer;
    }
}
