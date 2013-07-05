faceghost
=========

Facebook photo album scrapper that saves the photos locally.
It's a casper script, thus it depends on [CasperJS](http://casperjs.org/) and [PhantomJS](http://www.phantomjs.org/).


###Usage
1. Modify the config.json to use your facebook credentials.
2. Run the script in the console as follows:  
`$casperjs faceghost.js "https://www.facebook.com/media/set/?set=<identifier>"`
Options that can be used:  
 *  --folder=path/to/folder		Used to specify a directory to store the pictures  
 * --cookies-file=path/to/file	Used to specify a cookies file, dafaults to "cookies.txt" in the current directory

####Follow me on [twitter](https://twitter.com/tadeoval)