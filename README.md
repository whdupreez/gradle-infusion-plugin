# Gradle Infusion Plugin

## TODO

* Configuration
  * Templates
  * SrcDir
  * OutputDir
* index.html
* Link detection / generation
* Table of Contents
* Analytics
* Task: infusionInit

## Configuration

```
infusion {
	site {
		html {
			type 'html'
			srcDir 'src/site/html'
		}
		markdown {
			type 'markdown'
			srcDir 'src/site/markdown'
		}
		resources {
			type 'resource'
			srcDir 'src/site/resources'
		}
		templates {
			type 'template'
			srcDir 'src/site/templates'
		}
		properties {
			type 'properties'
			srcDir 'src/site/properties'
		}
	}
	templates {
		article {
			include = ''
			exclude = ''
			template = 'article.tpl.html'
		}
		blockdex {
			include = 'index.md'
			exclude = ''
			template = 'index.tpl.html'
		}
	}
	processors {
		markdown {
			class = ''
		}
	}
}
```