/*
 * Copyright (c) 2000 jPOS.org.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *    "This product includes software developed by the jPOS project
 *    (http://www.jpos.org/)". Alternately, this acknowledgment may
 *    appear in the software itself, if and wherever such third-party
 *    acknowledgments normally appear.
 *
 * 4. The names "jPOS" and "jPOS.org" must not be used to endorse
 *    or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    license@jpos.org.
 *
 * 5. Products derived from this software may not be called "jPOS",
 *    nor may "jPOS" appear in their name, without prior written
 *    permission of the jPOS project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE JPOS PROJECT OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the jPOS Project.  For more
 * information please see <http://www.jpos.org/>.
 */
package org.jpos.q2;

import org.jdom.Element;

/**
 * @author <a href="mailto:taherkordy@dpi2.dpi.net.ir">Alireza Taherkordi</a>
 * @author <a href="mailto:apr@cs.com.uy">Alejandro P. Revilla</a>
 * @version $Revision$ $Date$
 */
public class QBeanSupport implements QBean, QPersist, QBeanSupportMBean {
    Element persist;
    int state;
    Q2 server;
    boolean modified;
    String name;

    public QBeanSupport () {
        super();
    }
    public void setServer (Q2 server) {
        this.server = server;
    }
    public Q2 getServer () {
        return server;
    }
    public void setName (String name) {
        this.name = name;
    }
    public String getName () {
        return name;
    }
    public void init () {
        try {
            initService();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    public void start() {
        if (state != QBean.DESTROYED && 
            state != QBean.STOPPED   && 
            state != QBean.FAILED)
           return;

        this.state = QBean.STARTING;

        try {
           startService();
        } catch (Throwable t) {
           state = QBean.FAILED;
           t.printStackTrace();
           return;
        }
        state = QBean.STARTED;
    }
    public void stop () {
        if (state != QBean.STARTED)
           return;
        state = QBean.STOPPING;
        try {
           stopService();
        } catch (Throwable t) {
           state = QBean.FAILED;
           t.printStackTrace();
           return;
        }
        state = QBean.STOPPED;
    }
    public void destroy () {
        if (state == QBean.DESTROYED)
           return;
        if (state != QBean.STOPPED)
           stop();

        try {
           destroyService();
        }
        catch (Throwable t) {
           t.printStackTrace();
        }
        state = QBean.DESTROYED;
    }
    public int getState () {
        return state;
    }
    public void setState (int state) {
        this.state = state;
    }
    public void setPersist (Element persist) {
        this.persist = persist ;
    }
    public synchronized Element getPersist () {
        setModified (false);
        return persist;
    }
    public synchronized void setModified (boolean modified) {
        this.modified = modified;
    }
    public synchronized boolean isModified () {
        return modified;
    }
    protected boolean running () {
        return state == QBean.STARTING || state == QBean.STARTED;
    }
    protected void initService()    throws Exception {}
    protected void startService()   throws Exception {}
    protected void stopService()    throws Exception {}
    protected void destroyService() throws Exception {}
}
