package com.willydupreez.infusion.template;

import java.io.File;

public interface Template {

	void apply(File template, File content, File out);

}
