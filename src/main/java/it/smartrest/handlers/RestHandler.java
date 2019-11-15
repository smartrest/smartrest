package it.smartrest.handlers;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import it.smartrest.annotations.Body;
import it.smartrest.annotations.GET;
import it.smartrest.annotations.NotNull;
import it.smartrest.annotations.POST;
import it.smartrest.annotations.PUT;
import it.smartrest.annotations.Path;
import it.smartrest.annotations.PathParam;
import it.smartrest.annotations.Query;
import it.smartrest.core.HttpStatus;
import it.smartrest.core.ResponseBuilder;
import it.smartrest.responses.JSONReponse;
import it.smartrest.utils.Constants;
import it.smartrest.utils.URLUtils;

public class RestHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Parameter map that contains all the send parameter
		HashMap<String, Object> callParameterMap = new HashMap<String, Object>();
		// getting parameter from body
		callParameterMap.putAll(URLUtils.findParameters(exchange.getRequestBody().readAllBytes()));
		// getting parameter from url after the "?" char
		callParameterMap.putAll(URLUtils.findParameters(exchange.getRequestURI().getQuery()));
		
//		for (Method m : this.getClass().getMethods()) {
//			if (m.isAnnotationPresent(Path.class)) {
//				String paths[] = m.getAnnotation(Path.class).value().split("/");
//				String par[] = exchange.getRequestURI().getPath().split("/");
//				for (int i = 0; i < paths.length; i++) {
//					if(paths[i].contains("{") && paths[i].contains("}")) {
//						callParameterMap.put(paths[i].replaceAll("\\{", "").replaceAll("\\}", ""), par[i+1]);
//					}
//				}
//			}
//		}

		boolean methodFound = false;
		// Looping all the method of invoked class to find know annotated method
		for (Method m : this.getClass().getMethods()) {
			if ((m.isAnnotationPresent(POST.class)
					&& exchange.getRequestMethod().equalsIgnoreCase(POST.class.getSimpleName()))
					|| (m.isAnnotationPresent(GET.class)
							&& exchange.getRequestMethod().equalsIgnoreCase(GET.class.getSimpleName()))
					|| (m.isAnnotationPresent(PUT.class)
							&& exchange.getRequestMethod().equalsIgnoreCase(PUT.class.getSimpleName()))) {
				try {
					
					if (m.isAnnotationPresent(Path.class)) {
						String paths[] = m.getAnnotation(Path.class).value().split("/");
						String par[] = exchange.getRequestURI().getPath().split("/");
						for (int i = 0; i < paths.length; i++) {
							if(paths[i].contains("{") && paths[i].contains("}")) {
								callParameterMap.put(paths[i].replaceAll("\\{", "").replaceAll("\\}", ""), par[i+1]);
							}
						}
					}
					
					
					//Getting the parameter list of the method
					Object[] para = buildParameterFromAnnotation(m.getParameterAnnotations(), callParameterMap, m.getAnnotatedParameterTypes());
					
					Object response = m.invoke(this.getClass().getDeclaredConstructor().newInstance(), para);
					if(response instanceof JSONReponse)
						ResponseBuilder.okJSON((JSONReponse)response, exchange);
					else
						ResponseBuilder.ok(response.toString(), exchange);
					//founded method
					methodFound = true;
				} catch (IllegalAccessException | InvocationTargetException | InstantiationException
						| NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					String error = "Required parameter not present ";
					ResponseBuilder.buildRespose(HttpStatus.BAD_REQUEST, error, exchange);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//Check if a method was invoked
		if (!methodFound)
			ResponseBuilder.notImplementedMethod("Method " + exchange.getRequestMethod() + " not implemented", exchange,
					getImplementedMethod().toString());

	}

	/**
	 * Return the list of implemented method for the invoked service
	 * 
	 * @return
	 */
	private synchronized StringBuffer getImplementedMethod() {
		StringBuffer b = new StringBuffer();
		Arrays.asList(this.getClass().getMethods()).forEach(m -> {
			Arrays.asList(m.getAnnotations()).forEach(a -> {
				if (a.annotationType().getSimpleName().equalsIgnoreCase(POST.class.getSimpleName())
						|| a.annotationType().getSimpleName().equalsIgnoreCase(GET.class.getSimpleName())
						|| a.annotationType().getSimpleName().equalsIgnoreCase(PUT.class.getSimpleName())) {
					b.append(a.annotationType().getSimpleName() + ",");
				}
			});
		});

		return b;
	}

	/**
	 * Get the ordered parameter list from the annotation of called method
	 * 
	 * @param aAnnotation   List of parameter Annotation
	 * @param aUrlParameter Map of found parameter
	 * @return Ordered list of method parameter
	 * @throws Exception 
	 */
	private Object[] buildParameterFromAnnotation(Annotation aAnnotation[][], HashMap<String, Object> aUrlParameter, AnnotatedType aAnnotationTypes[]) throws Exception {
		ArrayList<Object[]> orderedParams = new ArrayList<Object[]>();
		// loop the parameter annotation to find the values in the map of sended
		// parameter
		int counter = 0;
		for (Iterator<Annotation[]> iterator = Arrays.asList(aAnnotation).iterator(); iterator.hasNext();) {
			Annotation[] annotation = iterator.next();
			boolean found = false;
			boolean notnull = false;
			
//			AnnotatedType type = aAnnotationTypes[counter].getType();
//			Type cls=  type.getType();

			for (Iterator<Annotation> iterator2 = Arrays.asList(annotation).iterator(); iterator2.hasNext();) {
				Annotation anno = iterator2.next();
				if (anno.annotationType().equals(NotNull.class))
					notnull = true;
			}

			for (Iterator<Annotation> iterator2 = Arrays.asList(annotation).iterator(); iterator2.hasNext();) {
				Annotation a = iterator2.next();
				// Check if present Query parameter
				if (a.annotationType().equals(Query.class)) {
					if (aUrlParameter.containsKey(((Query) a).value())) {
						Object p[] = { ((Query) a).value(), getTypedValue(aUrlParameter.get(((Query) a).value()),aAnnotationTypes[counter].getType()) };
						orderedParams.add(p);
						found = true;
					}
				}
				// Check if present Body parameter
				if (a.annotationType().equals(Body.class)) {
					if (aUrlParameter.containsKey(((Body) a).value())) {
						Object p[] = { ((Body) a).value(), getTypedValue(aUrlParameter.get(((Body) a).value()),aAnnotationTypes[counter].getType()) };
						orderedParams.add(p);
						found = true;
					}
				}
				
				// Check if present PathParam parameter
				if (a.annotationType().equals(PathParam.class)) {
					if (aUrlParameter.containsKey(((PathParam) a).value())) {
						Object p[] = { ((PathParam) a).value(), getTypedValue(aUrlParameter.get(((PathParam) a).value()),aAnnotationTypes[counter].getType()) };
						orderedParams.add(p);
						found = true;
					}
				}
			}

			if (!found && !notnull) {
				Object p[] = { "notfoundparam", null };
				orderedParams.add(p);
			}
			counter++;
		}

		Object[] p = new Object[orderedParams.size()];
		for (int i = 0; i < orderedParams.size(); i++)
			p[i] = orderedParams.get(i)[1];

		return p;
	}
	
	private Object getTypedValue(Object aValue, Type aType) throws Exception {
		if(aType == java.math.BigDecimal.class) {
			return new java.math.BigDecimal((java.lang.String)aValue);
		}
		if(aType == java.math.BigInteger.class) {
			return new java.math.BigInteger((java.lang.String)aValue);
		}
		if(aType == java.lang.String.class) {
			return (java.lang.String)aValue;
		}
		if(aType == java.lang.Double.class) {
			return java.lang.Double.valueOf((java.lang.String)aValue);
		}
		if(aType == java.lang.Float.class) {
			return java.lang.Float.valueOf((java.lang.String)aValue);
		}
		if(aType == java.lang.Integer.class) {
			return java.lang.Integer.valueOf((java.lang.String)aValue);
		}
		if(aType == java.lang.Long.class) {
			return java.lang.Long.valueOf((java.lang.String)aValue);
		}
		if(aType == java.lang.Boolean.class) {
			return java.lang.Boolean.valueOf((java.lang.String)aValue);
		}
		if(aType == java.util.Date.class) {
			DateFormat df = new SimpleDateFormat(Constants.JSON_DATE_FORMAT);
			return df.parse((java.lang.String)aValue);
		}
		
		
		return aValue;
	}

}
