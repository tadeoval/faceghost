/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Tadeo Valencia
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

var starttime = Date.now();
var casper = require('casper').create();
var config = require('./config');
var utils = require('utils');
var fs = require('fs');

var cookiesFile = casper.cli.get('cookies-file');
var folder = casper.cli.get('folder');
var url;

if (!casper.cli.has(0)) {
	casper.echo("You must provide the album's URL");
	usage();
} else {
	url = casper.cli.get(0);
}

// If no folder is specified, use current directory
if (folder == undefined) {
	folder = '';
	casper.echo('No folder specified, using current directory');
} else if(folder.length > 0) {
	folder += '/';
}

// Try to use cookies to by-pass a call to login
var cookiesData;

if (cookiesFile && fs.exists(cookiesFile)) {
	cookiesData = fs.read(cookiesFile);
} else if (fs.exists('cookies.txt')) {
	cookiesData = fs.read('cookies.txt');
}

if (cookiesData) {
	try {
		phantom.cookies = JSON.parse(cookiesData);
	} catch (ex) {
		casper.echo('There was an error trying to parse the cookies file,');
		casper.echo('please verify that it is a cookies file and it is in JSON format.');
		casper.echo('Continuing script execution...');
	}	
}

function usage(){
	casper.echo('e.g. \n$casperjs faceghost.js "https://www.facebook.com/media/set/?set=<identifier>"');
	casper.echo('Options that can be used:');
	casper.echo('--folder=path/to/folder		Used to specify a directory to store the pictures');
	casper.echo('--cookies-file=path/to/file	Used to specify a cookies file, dafaults to "cookies.txt" in the current directory');
	casper.exit();
}

var album = url;
var pics = [];

// Start navigation in casper
casper.start().then(function() {
    this.echo("Starting, accessing FB...");
});

// Open FB and login
casper.thenOpen('https://facebook.com', function() {
	if (this.exists('#login_form')) {
		this.echo('Accesed FB, now logging in...');
		this.click('input[name="persistent"]');
		this.fill('#login_form', {
			'email': config.FB.name,
			'pass' : config.FB.password
		},true);
	}
});

casper.then(function(){
	this.echo('Logged in, now opening album...');
});

casper.thenOpen(album, function(){
	var title = this.fetchText('.fbPhotoAlbumTitle');
	this.echo('Opened album ' + title);
	// Opens the link in the first pic of the album to start fetching
	this.open(this.getElementAttribute('.uiMediaThumb', 'href'));
});

casper.then(function(){
	savePic.call(this);
});

/**
 * Save the pic
 * It must be called using the method Function.prototype.call
 * e.g. savePic.call(casper);
 */
function savePic(){
	// Add this as a step to the navigation stack, otherwise it won't wait for the
	// next pic's page to load, thus terminating the execution of the script
	this.then(function(){
		var imgSrc = this.getElementAttribute('#fbPhotoImage', 'src');
		var name = imgSrc.substring(imgSrc.lastIndexOf('/')+1);

		if (pics.indexOf(name) < 0) {
			this.echo('Saving a pic...');
			this.download(imgSrc, folder + name);
			pics.push(name);
			this.echo('Saved pic #' + pics.length);
			this.emit('pic.saved');
		} else {
			this.echo("This pic was already saved, we've finished");
			var cookies = JSON.stringify(phantom.cookies);
			fs.write("cookies.txt", cookies, 644);
			var time = Date.now() - starttime;
			this.echo('Done in: ' + (time/1000) + ' segs.');
			this.exit();
		}
	});
}

/**
 * Just opens the page
 * It must be called using the method Function.prototype.call
 * e.g. openLink.call(casper, arg);
 */
function openLink(link){
	// The permPage seems to be used to avoid full page load, removing it doesn't seem to affect our goal
	//link = link.replace('&permPage=1', '');
	this.start(link, function() {
		this.echo('Accessed next pic');
	});
}

/**
 * Call the looping methods when pic.saved is triggered
 */
casper.on('pic.saved', function() {
	this.echo('Fetching next pic...');
	openLink.call(this, this.getElementAttribute('.photoPageNextNav', 'href'));
	savePic.call(this);
});

casper.run();