/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.seam.faces.security;

import javax.enterprise.event.Observes;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.inject.Inject;
import org.jboss.logging.Logger;
import org.jboss.seam.faces.transaction.TransactionPhaseListener;
import org.jboss.seam.faces.viewmeta.ViewMetaStore;
import org.jboss.seam.solder.el.Expressions;

/**
 *
 * @author bleathem
 */
public class ViewMetaRestrictEnforcer
{
    private static final Logger log = Logger.getLogger(TransactionPhaseListener.class);

   @Inject
   private ViewMetaStore metaStore;
   @Inject
   private Expressions expressions;

   public void enforce (@Observes PostConstructViewMapEvent event)
   {
      log.info("PostConstructViewMapEvent");
      Restrict annotation = metaStore.getDataForCurrentViewId(Restrict.class);
      if (annotation == null)
      {
         log.info("Annotation is null");
         return;
      }
      log.info("Evaluating Annotation");
      String el = annotation.value();
      Boolean allowed = expressions.evaluateMethodExpression(el, Boolean.class);
      if (allowed)
      {
          log.info("Access allowed");
          return;
      }
      else
      {
          log.info("Access denied");
          throw new AbortProcessingException("Access denied");
      }
   }
}
