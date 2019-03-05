const
DevPic = {
	'SW3' : "res/images/交换机.png",
	'SW2' : "res/images/交换机.png",
	'SW' : "res/images/交换机.png",
	'RT' : "res/images/路由器.png",
	'PC' : "res/images/pc.png"
};
const
DevSize = {
	'SW3' : {
		'x' : 47,
		'y' : 34
	},
	'SW2' : {
		'x' : 47,
		'y' : 34
	},
	'SW' : {
		'x' : 47,
		'y' : 34
	},
	'RT' : {
		'x' : 47,
		'y' : 35
	},
	'PC' : {
		'x' : 30,
		'y' : 26
	}
};
const
DevConvert = {
	'SW3' : 'SW3',
	'SW2' : 'SW2',
	'SW' : 'SW',
	'RT' : 'RT',
	'PC' : 'PC'
};
const
DevPositionDefault = {
	'SW3' : {
		'x' : 456,
		'y' : 585
	},
	'SW2' : {
		'x' : 564,
		'y' : 583
	},
	'RT' : {
		'x' : 672,
		'y' : 580
	},
	'PC' : {
		'x' : 785,
		'y' : 586
	}
};
const
DevNumPosition = {
	'SW3' : {
		'x' : 512,
		'y' : 623
	},
	'SW2' : {
		'x' : 621,
		'y' : 625
	},
	'RT' : {
		'x' : 715,
		'y' : 624
	},
	'PC' : {
		'x' : 813,
		'y' : 623
	}
};
const
HeightBorder = 500;
const
WidthBorder = 350;

var topo = topo || {
	canvas : null,
	names : [], // the name of the devices ,split with ##
	nums : [], // the id of the devices , split with ##
	setup : [], // the above devices whether setup
	ports : [], // the port of the devices , split with ## and @
	drags : [], //
	left_nums : [],
	right_nums : [],
	left_ports : [],
	right_ports : [],
	stage : null,
	scene : null,
	zindex : 999,
	operatorElements : 0,
	operatorNum : -1,
	SW3_num : 0,
	SW2_num : 0,
	RT_num : 0,
	PC_num : 0,
	SW_name : 0,
	RT_name : 0,
	PC_name : 0,
	PC_count : new Array('a', 'b', 'c', 'd'),
	SW3_num_node : new JTopo.TextNode(0),
	SW2_num_node : new JTopo.TextNode(0),
	RT_num_node : new JTopo.TextNode(0),
	PC_num_node : new JTopo.TextNode(0),
	msgNode : new JTopo.TextNode("")
};

topo.init = function(canvas, name_str, num_str, port_str) {
	topo.canvas = canvas;
	console.log(name_str, num_str, port_str);
	topo.initDevices(name_str, num_str, port_str);
	// 默认不允许编辑
	// enableTopo();

	console.log("init ok");
}

topo.clone = function(obj) {
	// obj includes the seven string of the topo image
	// obj includes the position of the devices
	topo.restart();
	topo.showFullDevices(obj.position);
	topo.linkFullLinks(obj.leftNUM_Str, obj.rightNUM_Str, obj.leftport_Str,
			obj.rightport_Str);
}

topo.initDevices = function(name_str, num_str, port_str) {
	topo.names = name_str.split('##');
	topo.nums = num_str.split('##');
	var tmp = port_str.split('##');
	for (var i = 0; i < tmp.length; ++i)
		topo.ports[i] = tmp[i].split('@');

	for (var i = 0; i < topo.names.length; ++i)
		topo.setup[i] = false;
}

topo.showFullDevices = function(position) {
	// alert(position);
	if (position == undefined || position == null || position == "") {
		for (var i = 0; i < topo.names.length; ++i) {
			topo.create(topo.names[i]);
		}
	} else {
		position = position.split(',');
		for (var i = 0; i < topo.names.length; ++i) {
			var poss = position[i].split(' ');
			topo.create(topo.names[i], eval(poss[0] * topo.getScale()),
					eval(poss[1] * topo.getScale()));// **20171111蒋家盛新增
														// 拓扑图可用百分比大小***//
		}
	}
}

topo.show = function() {
	var jsonStr = null;
	// initialize
	if (jsonStr == null) {
		topo.stage = new JTopo.Stage(topo.canvas);
		topo.scene = new JTopo.Scene(topo.stage);
		topo.scene.mode = "select";
		topo.scene.background = 'res/images/bg_new.png';
		topo.addFunc(topo.scene);
	} else {
		topo.stage = JTopo.createStageFromJson(jsonStr, topo.canvas);
		topo.scene = topo.stage.childs[0];
	}
	console.log("show ok");
}

topo.toJson = function() {
	console.log(topo.stage.toJson());
}

topo.linkFullLinks = function(leftNUM_Str, rightNUM_Str, leftport_Str,
		rightport_Str) {
	var ln = leftNUM_Str.split('##');
	var rn = rightNUM_Str.split('##');
	var lp = leftport_Str.split('##');
	var rp = rightport_Str.split('##');
	var scenePorts = topo.scene.findElements(function(e) {
		return e.type == 'port';
	});
	for (var i = 0; i < ln.length; ++i) {
		for (var j = 0; j < scenePorts.length; ++j) {
			if (topo.nums[scenePorts[j].father.index] == ln[i]
					&& topo.ports[scenePorts[j].father.index][scenePorts[j].index] == lp[i]) {
				for (var k = 0; k < scenePorts.length; ++k) {
					if (topo.nums[scenePorts[k].father.index] == rn[i]
							&& topo.ports[scenePorts[k].father.index][scenePorts[k].index] == rp[i]) {
						topo.link(scenePorts[j], scenePorts[k]);
					}
				}
			}
		}
	}
}

topo.link = function(beginNode, endNode) {
	var l = new JTopo.Link(beginNode, endNode);
	l.opindex = topo.operatorElements;
	topo.operatorElements++;
	beginNode.linked = true;
	endNode.linked = true;
	topo.scene.add(l);
}

topo.addFunc = function(scene) {

	console.log("here");
	topo.msgNode.type = 'msg';
	topo.msgNode.dragable = false;
	topo.msgNode.zIndex = topo.zindex--;
	topo.msgNode.setBound(30 * topo.getScale(), 30 * topo.getScale());// **20171111蒋家盛新增
																		// 拓扑图可用百分比大小***//
	scene.add(topo.msgNode);

	var beginNode = null;
	var tempNodeA = new JTopo.Node('tempA');
	;
	tempNodeA.setSize(1, 1);

	var tempNodeZ = new JTopo.Node('tempZ');
	;
	tempNodeZ.setSize(1, 1);

	var link = new JTopo.Link(tempNodeA, tempNodeZ);

	scene.mouseup(function(e) {
		/** **设备吸附到指定位置*** */
		if (e.target && e.target.type == 'dev' && e.target.name == 'SW3'
				&& e.x > WidthBorder * topo.getScale()
				&& e.y > HeightBorder * topo.getScale()) {// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
			e.target.setLocation(DevPositionDefault['SW3'].x * topo.getScale(),
					DevPositionDefault['SW3'].y * topo.getScale());// **20171111蒋家盛新增
																	// 拓扑图可用百分比大小***//
		} else if (e.target && e.target.type == 'dev' && e.target.name == 'SW2'
				&& e.x > WidthBorder * topo.getScale()
				&& e.y > HeightBorder * topo.getScale()) {// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
			e.target.setLocation(DevPositionDefault['SW2'].x * topo.getScale(),
					DevPositionDefault['SW2'].y * topo.getScale());// **20171111蒋家盛新增
																	// 拓扑图可用百分比大小***//
		} else if (e.target && e.target.type == 'dev' && e.target.name == 'RT'
				&& e.x > WidthBorder * topo.getScale()
				&& e.y > HeightBorder * topo.getScale()) {// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
			e.target.setLocation(DevPositionDefault['RT'].x * topo.getScale(),
					DevPositionDefault['RT'].y * topo.getScale());// **20171111蒋家盛新增
																	// 拓扑图可用百分比大小***//
		} else if (e.target && e.target.type == 'dev' && e.target.name == 'PC'
				&& e.x > WidthBorder * topo.getScale()
				&& e.y > HeightBorder * topo.getScale()) {// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
			e.target.setLocation(DevPositionDefault['PC'].x * topo.getScale(),
					DevPositionDefault['PC'].y * topo.getScale());// **20171111蒋家盛新增
																	// 拓扑图可用百分比大小***//
		}
		/** **设备吸附到指定位置*** */

		/** **连线*** */
		else if (e.target && e.target.layout) {
			JTopo.layout.layoutNode(scene, e.target, true);
		}

		else if (e.button == 2) {
			scene.remove(link);
			beginNode = null;
			return;
		} else if (e.target != null && e.target.type == 'port'
				&& !e.target.linked) {
			if (beginNode == null) {
				beginNode = e.target;
				scene.add(link);
				tempNodeA.setLocation(e.x, e.y);
				tempNodeZ.setLocation(e.x, e.y);
				topo.msgNode.text = "未保存拓扑";
			} else if (beginNode !== e.target
					&& beginNode.father != e.target.father) {
				topo.link(beginNode, e.target);
				scene.remove(link);
				beginNode = null;
			} else {
				scene.remove(link);
				beginNode = null;
			}
		} else {
			scene.remove(link);
			beginNode = null;
		}
		/** **连线*** */
	});

	scene.mousedown(function(e) {
		if (e.target == null || e.target === beginNode || e.target === link) {
			scene.remove(link);
			beginNode = null;
		}
	});
	scene.mousemove(function(e) {
		tempNodeZ.setLocation(e.x, e.y);
	});

	/** **选中撤销*** */
	scene.click(function(e) {
		if (e.target && e.target.elementType == 'link'
				&& e.target.nodeA.linked == true
				&& e.target.nodeZ.linked == true) {
			topo.operatorNum = e.target.opindex;
		} else if (e.target && e.target.type == 'port'
				&& e.target.linked == true) {
			topo.scene.findElements(function(a) {
				if (a.elementType == 'link' && a.nodeA.linked == true
						&& a.nodeZ.linked == true
						&& (a.nodeA == e.target || a.nodeZ == e.target)) {
					topo.operatorNum = a.opindex;
				}
			});
		}
	});
	/** **选中撤销*** */

	/** **收纳设备*** */
	scene
			.mousedrag(function(e) {
				if (e.target && e.target.type == 'dev'
						&& e.x > WidthBorder * topo.getScale()
						&& e.y > HeightBorder * topo.getScale()
						&& topo.drags[e.target.index]) {// **20171111蒋家盛新增
														// 拓扑图可用百分比大小***//
					for (var i = 0; i < e.target.ports.length; ++i) {
						topo.scene
								.findElements(function(a) {
									if (a.elementType == 'link'
											&& a.nodeA.linked == true
											&& a.nodeZ.linked == true
											&& (a.nodeA == e.target.ports[i] || a.nodeZ == e.target.ports[i])) {
										topo.operatorNum = a.opindex;
										topo.undo();
									}
								});
						scene.remove(e.target.ports[i]);
					}
					if (e.target.name == 'SW3') {
						topo.SW3_num_node.text = ++topo.SW3_num;
					} else if (e.target.name == 'SW2') {
						topo.SW2_num_node.text = ++topo.SW2_num;
					} else if (e.target.name == 'RT') {
						topo.RT_num_node.text = ++topo.RT_num;
					} else if (e.target.name == 'PC') {
						topo.PC_num_node.text = ++topo.PC_num;
					}
					topo.drags[e.target.index] = false;
				} else if (e.target
						&& e.target.type == 'dev'
						&& (e.x <= WidthBorder * topo.getScale() || e.y <= HeightBorder
								* topo.getScale())) {// **20171111蒋家盛新增
														// 拓扑图可用百分比大小***//
					if (!topo.drags[e.target.index]) {
						for (var i = 0; i < e.target.ports.length; ++i) {
							var portX = DevSize[e.target.name].x / 2 + 5
									- (e.target.ports.length * 11) + i * 22;
							var portY = DevSize[e.target.name].y / 2 + 34;
							portX = portX * topo.getScale();// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
							portY = portY * topo.getScale();// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
							// console.log("portX:"+portX+"
							// portY:"+portY);//*****//
							e.target.ports[i].setLocation(e.target.x + portX,
									e.target.y + portY);
							e.target.ports[i].dragable = false;
							scene.add(new JTopo.Link(e.target,
									e.target.ports[i]));
							scene.add(e.target.ports[i]);
						}
						if (e.target.name == 'SW3') {
							topo.SW3_num_node.text = --topo.SW3_num;
						} else if (e.target.name == 'SW2') {
							topo.SW2_num_node.text = --topo.SW2_num;
						} else if (e.target.name == 'RT') {
							topo.RT_num_node.text = --topo.RT_num;
						} else if (e.target.name == 'PC') {
							topo.PC_num_node.text = --topo.PC_num;
						}
						topo.drags[e.target.index] = true;
					} else {
						for (var i = 0; i < e.target.ports.length; ++i) {
							var portX = DevSize[e.target.name].x / 2 + 5
									- (e.target.ports.length * 11) + i * 22;
							var portY = DevSize[e.target.name].y / 2 + 34;
							portX = portX * topo.getScale();// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
							portY = portY * topo.getScale();// **20171111蒋家盛新增
															// 拓扑图可用百分比大小***//
							e.target.ports[i].setLocation(e.target.x + portX,
									e.target.y + portY);
						}
					}
				}
			});
	/** **收纳设备*** */
}

topo.create = function(dev, posx, posy) {

	var node = null;
	var str = DevConvert[dev];
	if (str == null || str == undefined)
		return false;
	var flag = -1;
	for (var i = 0; i < topo.names.length; ++i)
		if (topo.names[i] == str && !topo.setup[i]) {
			flag = i;
			topo.setup[i] = true;
			str = str + '(' + topo.nums[i] + ')';
			break;
		}

	if (flag == -1) {
		console.log('resource ' + str + ' is not enough');
		return;
	}

	if (posx == undefined || posx == null) {
		posx = DevPositionDefault[dev].x * topo.getScale();
		posy = DevPositionDefault[dev].y * topo.getScale();
	}

	node = new JTopo.Node(str);
	node.name = dev;
	node.type = 'dev';
	node.index = flag;
	node.opindex = topo.operatorElements;
	node.zIndex = topo.zindex--;
	node.textPosition = "Top_Center";
	node.layout = {
		type : 'tree',
		direction : 'bottom',
		width : 22 * topo.getScale(),
		height : 40 * topo.getScale()
	};// **20171111蒋家盛新增 拓扑图可用百分比大小***//
	node.setBound(posx, posy, DevSize[dev].x * topo.getScale(), DevSize[dev].y
			* topo.getScale());// **20171111蒋家盛新增 拓扑图可用百分比大小***//
	node.setImage(DevPic[dev]);// 加参数“true”可以显示原图片大小，否则显示节点大小
	topo.scene.add(node);

	node.ports = new Array();
	for (var i = 0; i < topo.ports[flag].length; ++i) {
		node.ports[i] = new JTopo.Node(topo.ports[flag][i]);
		node.ports[i].opindex = topo.operatorElements;
		node.ports[i].type = 'port';
		node.ports[i].index = i;
		node.ports[i].father = node;
		node.ports[i].linked = false;
		node.ports[i].setSize(12 * topo.getScale(), 12 * topo.getScale());// **20171111蒋家盛新增
																			// 拓扑图可用百分比大小***//
		var portX = DevSize[dev].x / 2 + 5 - (topo.ports[flag].length * 11) + i
				* 22;
		var portY = DevSize[dev].y / 2 + 34;
		portX = portX * topo.getScale();// **20171111蒋家盛新增 拓扑图可用百分比大小***//
		portY = portY * topo.getScale();// **20171111蒋家盛新增 拓扑图可用百分比大小***//
		node.ports[i].setLocation(node.x + portX, node.y + portY);
	}

	if (posx > WidthBorder * topo.getScale()
			&& posy > HeightBorder * topo.getScale()) {// **20171111蒋家盛新增
														// 拓扑图可用百分比大小***//
		if (dev == 'SW3') {
			topo.SW3_num++;
		} else if (dev == 'SW2') {
			topo.SW2_num++;
		} else if (dev == 'RT') {
			topo.RT_num++;
		} else if (dev == 'PC') {
			topo.PC_num++;
		}
		topo.drags[flag] = false;
	} else {
		for (var i = 0; i < topo.ports[flag].length; ++i) {
			node.ports[i].dragable = false;
			topo.scene.add(new JTopo.Link(node, node.ports[i]));
			topo.scene.add(node.ports[i]);
		}
		topo.drags[flag] = true;
	}

	if (dev == 'SW3' || dev == 'SW2') {
		topo.SW_name++;
		node.text = "SW" + topo.SW_name;
	} else if (dev == 'RT') {
		topo.RT_name++;
		node.text = "RT" + topo.RT_name;
	} else if (dev == 'PC') {// 20171014蒋家盛修补掉线重连命名undefined问题
		node.text = "PC" + topo.PC_count[topo.PC_name];
		topo.PC_name = topo.PC_name + 1;
	}

	topo.SW3_num_node.text = topo.SW3_num;
	topo.SW3_num_node.type = 'msg';
	topo.SW3_num_node.setBound(DevNumPosition['SW3'].x * topo.getScale(),
			DevNumPosition['SW3'].y * topo.getScale(), 6 * topo.getScale(),
			6 * topo.getScale());// **20171111蒋家盛新增 拓扑图可用百分比大小***//
	topo.scene.add(topo.SW3_num_node);

	topo.SW2_num_node.text = topo.SW2_num;
	topo.SW2_num_node.type = 'msg';
	topo.SW2_num_node.setBound(DevNumPosition['SW2'].x * topo.getScale(),
			DevNumPosition['SW2'].y * topo.getScale(), 6 * topo.getScale(),
			6 * topo.getScale());// **20171111蒋家盛新增 拓扑图可用百分比大小***//
	topo.scene.add(topo.SW2_num_node);

	topo.RT_num_node.text = topo.RT_num;
	topo.RT_num_node.type = 'msg';
	topo.RT_num_node.setBound(DevNumPosition['RT'].x * topo.getScale(),
			DevNumPosition['RT'].y * topo.getScale(), 6 * topo.getScale(),
			6 * topo.getScale());// **20171111蒋家盛新增 拓扑图可用百分比大小***//
	topo.scene.add(topo.RT_num_node);

	topo.PC_num_node.text = topo.PC_num;
	topo.PC_num_node.type = 'msg';
	topo.PC_num_node.setBound(DevNumPosition['PC'].x * topo.getScale(),
			DevNumPosition['PC'].y * topo.getScale(), 6 * topo.getScale(),
			6 * topo.getScale());// **20171111蒋家盛新增 拓扑图可用百分比大小***//
	topo.scene.add(topo.PC_num_node);

	topo.operatorElements++;
}

topo.getDevPos = function() {
	var result = [];
	topo.scene.findElements(function(e) {
		if (e.type == 'dev') {
			var tempx = e.x / topo.getScale();// **20171111蒋家盛新增
												// 拓扑图可用百分比大小***//
			var tempy = e.y / topo.getScale();// **20171111蒋家盛新增
												// 拓扑图可用百分比大小***//
			result.push(tempx.toString() + ' ' + tempy.toString());
		}

		return false;
	});
	result = result.toString();
	return result;
}
// 20170422去除浏览器缓存影响
topo.restart = function() {
	// ZhangDongya 2017/4/22
	if (topo.scene == null) {
		topo.stage = new JTopo.Stage(topo.canvas);
		topo.scene = new JTopo.Scene(topo.stage);
		topo.scene.background = 'res/images/bg_new.png';
	} else {
		topo.scene.clear();
	}
	topo.msgNode.text = "";
	topo.operatorElements = 0;
	topo.left_nums = [];
	topo.right_nums = [];
	topo.left_ports = [];
	topo.right_ports = [];
	topo.SW3_num = 0;
	topo.SW2_num = 0;
	topo.RT_num = 0;
	topo.PC_num = 0;
	topo.PC_name = 0;
	topo.SW_name = 0;
	topo.RT_name = 0;
	topo.addFunc(topo.scene);// 20170119蒋家盛修改使msgNode显示出来
	for (var i = 0; i < topo.setup.length; ++i)
		topo.setup[i] = false;
}

topo.undo = function() {
	if (topo.operatorNum == -1) {
		return;
	}
	var removeList = topo.scene.findElements(function(e) {
		return e.opindex == topo.operatorNum;
	});
	removeList.forEach(function(e) {
		if (e.elementType == 'link') {
			if (e.nodeA)
				e.nodeA.linked = false;
			if (e.nodeZ)
				e.nodeZ.linked = false;
		}
		if (e.type == 'dev') {
			topo.setup[e.index] = false;
		}
		topo.scene.remove(e);
	});
	topo.operatorNum = -1;
	topo.msgNode.text = "未保存拓扑";
}

/*
 * topo.submit = function(){ var linkList = topo.scene.findElements(function(e){
 * if (e.elementType == 'link'&&e.nodeA.linked==true&&e.nodeZ.linked==true){
 * return true; } });
 * 
 * topo.left_nums = []; topo.right_nums = []; topo.left_ports = [];
 * topo.right_ports = [];
 * 
 * linkList.forEach(function(e){ if(topo.nums[e.nodeA.father.index]<topo.nums[e.nodeZ.father.index]){
 * topo.left_nums.push( topo.nums[e.nodeA.father.index] ); topo.right_nums.push(
 * topo.nums[e.nodeZ.father.index] ); topo.left_ports.push(
 * topo.ports[e.nodeA.father.index][e.nodeA.index] ); topo.right_ports.push(
 * topo.ports[e.nodeZ.father.index][e.nodeZ.index] ); } else{
 * topo.right_nums.push( topo.nums[e.nodeA.father.index] ); topo.left_nums.push(
 * topo.nums[e.nodeZ.father.index] ); topo.right_ports.push(
 * topo.ports[e.nodeA.father.index][e.nodeA.index] ); topo.left_ports.push(
 * topo.ports[e.nodeZ.father.index][e.nodeZ.index] ); } }); //以下是提交连线的结果
 * //发送给后台编辑 var position = topo.getDevPos(); var leftNUM_Str =
 * topo.left_nums.join("##"); var rightNUM_Str = topo.right_nums.join("##"); var
 * leftport_Str = topo.left_ports.join("##"); var rightport_Str =
 * topo.right_ports.join("##");
 * 
 * console.log(position + ' '+leftNUM_Str + ' '+ rightNUM_Str + ' ' +
 * leftport_Str + ' ' + rightport_Str);
 * 
 *  }
 */

topo.submit = function(expTaskOrder) { // expTaskOrder为0时是初始拓扑
										// //20171114蒋家盛将提交拓扑与保存至数据库 合并为一个操作

	var linkList = topo.scene.findElements(function(e) {
		if (e.elementType == 'link' && e.nodeA.linked == true
				&& e.nodeZ.linked == true) {
			return true;
		}
	});

	topo.left_nums = [];
	topo.right_nums = [];
	topo.left_ports = [];
	topo.right_ports = [];

	linkList.forEach(function(e) {
		if (topo.nums[e.nodeA.father.index] < topo.nums[e.nodeZ.father.index]) {
			topo.left_nums.push(topo.nums[e.nodeA.father.index]);
			topo.right_nums.push(topo.nums[e.nodeZ.father.index]);
			topo.left_ports
					.push(topo.ports[e.nodeA.father.index][e.nodeA.index]);
			topo.right_ports
					.push(topo.ports[e.nodeZ.father.index][e.nodeZ.index]);
		} else {
			topo.right_nums.push(topo.nums[e.nodeA.father.index]);
			topo.left_nums.push(topo.nums[e.nodeZ.father.index]);
			topo.right_ports
					.push(topo.ports[e.nodeA.father.index][e.nodeA.index]);
			topo.left_ports
					.push(topo.ports[e.nodeZ.father.index][e.nodeZ.index]);
		}
	});

	if (topo.left_nums.length == 0 || topo.right_nums == 0
			|| topo.left_ports == 0 || topo.right_ports == 0) {
		alert("请至少连一条线");
		return;
	}

	var position = topo.getDevPos();
	var leftNUM_Str = topo.left_nums.join("##");
	var rightNUM_Str = topo.right_nums.join("##");
	var leftport_Str = topo.left_ports.join("##");
	var rightport_Str = topo.right_ports.join("##");

	console.log(position + ' ' + leftNUM_Str + ' ' + rightNUM_Str + ' '
			+ leftport_Str + ' ' + rightport_Str);

	topoSend(position, leftNUM_Str, rightNUM_Str, leftport_Str, rightport_Str);
	topoSaveToDatabase(position, leftNUM_Str, rightNUM_Str, leftport_Str,
			rightport_Str, expTaskOrder);
	topo.msgNode.text = "";
}

topo.getScale = function() {// 根据拓扑图实际大小求出与850*650的比例
	// console.log("Ratio:"+topo.canvas.clientWidth/850);
	return topo.canvas.clientWidth / 850;
}