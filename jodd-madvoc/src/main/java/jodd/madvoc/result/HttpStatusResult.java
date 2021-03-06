// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package jodd.madvoc.result;

import jodd.madvoc.ActionRequest;
import jodd.util.StringUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * Sets HTTP status or error code.
 */
public class HttpStatusResult extends BaseActionResult<String> {

	public static final String NAME = "http";

	public HttpStatusResult() {
		super(NAME);
	}

	public void render(ActionRequest actionRequest, String resultValue) throws Exception {
		boolean isError = false;
		if (StringUtil.endsWithChar(resultValue, '!')) {
			isError = true;
			resultValue = resultValue.substring(0, resultValue.length() - 1);
		}

		HttpServletResponse response = actionRequest.getHttpServletResponse();
		if (isError) {
			response.sendError(Integer.parseInt(resultValue));
		} else {
			response.setStatus(Integer.parseInt(resultValue));
		}

	}
}