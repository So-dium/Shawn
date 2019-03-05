const DevPic = {'SW3':"res/images/switch.png",'SW2':"res/images/switch.png",'SW':"res/images/switch.png",'RT':"res/images/router.png",'PC':"res/images/pc.png"};
const DevSize = {'SW3':{'x':47,'y':34},'SW2':{'x':47,'y':34},'SW':{'x':47,'y':34},'RT':{'x':47,'y':35},'PC':{'x':30,'y':26}};
const DevConvert = {'SW3':'SW3', 'SW2':'SW2', 'SW':'SW', 'RT':'RT', 'PC':'PC'}


var topo = topo || {
  canvas:null,
  names:[],  // the name of the devices ,split with ##
  nums:[],   // the id of the devices , split with ##
  setup:[],  // the above devices whether setup
  ports:[],  // the port of the devices , split with ## and @
  left_nums:[],
  right_nums:[],
  left_ports:[],
  right_ports:[],
  stage:null,
  scene:null,
  operatorElements:0
};

topo.init = function(canvas,name_str,num_str,port_str){
	//alert("11111topo.init ");
  this.canvas = canvas;
  this.initDevices(name_str,num_str,port_str);
  //默认不允许编辑
  enableTopo();
}

topo.clone = function(obj){
  //obj includes the seven string of the topo image 
  //obj includes the position of the devices
  this.restart();
  //this.initDevices(obj.name_str , obj.num_str , obj.port_str)
  this.showFullDevices(obj.position);
  this.linkFullLinks(obj.leftNUM_Str , obj.rightNUM_Str , obj.leftport_Str , obj.rightport_Str);
}

topo.initDevices = function(name_str,num_str,port_str){

  this.names = name_str.split('##');
  this.nums = num_str.split('##');
  var tmp = port_str.split('##');
  for (var i = 0;i < tmp.length;++i)
	  this.ports[i] = tmp[i].split('@');

  for (var i = 0;i < this.names.length;++i)
  		this.setup[i] = false;
}

topo.showFullDevices = function(position){
  position = position.split(',')
  for (var i = 0;i < this.names.length;++i){
	  var poss = position[i].split(' ');
	  this.create(this.names[i] , eval(poss[0]) , eval(poss[1]) );	  
  }
}

topo.show = function(){
  var jsonStr = null;
  //alert("11111topo.show ");
  //initialize
  if (jsonStr == null)
  {
    this.stage = new JTopo.Stage(this.canvas);
    this.scene = new JTopo.Scene(this.stage);
    this.scene.background = 'res/images/bg.jpg';
    this.addFunc(this.scene);
  }
  else
  {
    this.stage = JTopo.createStageFromJson(jsonStr, this.canvas);
    this.scene = this.stage.childs[0];
  }
  
}

topo.toJson = function(){
	console.log(this.stage.toJson());
}

topo.linkFullLinks = function(leftNUM_Str , rightNUM_Str , leftport_Str , rightport_Str){
	var ln = leftNUM_Str.split('##');
	var rn = rightNUM_Str.split('##');
	var lp = leftport_Str.split('##');
	var rp = rightport_Str.split('##');
	var scenePorts = this.scene.findElements(function(e){
						return e.type == 'port';
					 });
	for (var i = 0;i < ln.length;++i)
		for (var j = 0;j < scenePorts.length;++j)
			if ( this.nums[scenePorts[j].father.index] == ln[i] && this.ports[scenePorts[j].father.index][scenePorts[j].index] == lp[i] )
				for (var k = 0;k < scenePorts.length;++k)
					if ( this.nums[scenePorts[k].father.index] == rn[i] && this.ports[scenePorts[k].father.index][scenePorts[k].index] == rp[i] ){
						this.link(scenePorts[j] , scenePorts[k]);
					}
}

topo.link = function(beginNode , endNode){
	var l = new JTopo.Link(beginNode, endNode);
	l.opindex = this.operatorElements;
	this.operatorElements++;
	beginNode.linked = true;
	endNode.linked = true;
	this.left_nums.push( this.nums[beginNode.father.index] );
	this.right_nums.push( this.nums[endNode.father.index] );
	this.left_ports.push( this.ports[beginNode.father.index][beginNode.index] );
	this.right_ports.push( this.ports[endNode.father.index][endNode.index] );
	this.scene.add(l);				
}

topo.addFunc = function(scene){
	var that = this;

	var msgNode = new JTopo.TextNode("单击端口连线");
		msgNode.type = 'msg';
		msgNode.zIndex++;
		msgNode.setBound(200, 50);
		msgNode.setLocation(30,30);
		scene.add(msgNode);

	var beginNode = null;
	var tempNodeA = new JTopo.Node('tempA');;
	tempNodeA.setSize(1, 1);

	var tempNodeZ = new JTopo.Node('tempZ');;
	tempNodeZ.setSize(1, 1);

	var link = new JTopo.Link(tempNodeA, tempNodeZ);

	scene.mouseup(function(e){
		if(e.target && e.target.layout){
         JTopo.layout.layoutNode(scene, e.target, true);
		}

		if(e.button == 2){
			scene.remove(link);
			beginNode = null;
			return;
		}
		if(e.target != null && e.target.type == 'port' && !e.target.linked){
			if(beginNode == null){
				beginNode = e.target;
				scene.add(link);
				tempNodeA.setLocation(e.x, e.y);
				tempNodeZ.setLocation(e.x, e.y);
			}else if(beginNode !== e.target && beginNode.father != e.target.father){
				that.link(beginNode , e.target);
				scene.remove(link);
				beginNode = null;
			}else{
				scene.remove(link);
				beginNode = null;
			}
		}else{
			scene.remove(link);
			beginNode = null;
		}
	});

	scene.mousedown(function(e){
		if(e.target == null || e.target === beginNode || e.target === link){
			scene.remove(link);
			beginNode = null;
		}
	});
	scene.mousemove(function(e){
		tempNodeZ.setLocation(e.x, e.y);
	});
}

topo.create = function(dev , posx , posy){

  var node = null;
  var str = DevConvert[dev];
  if (str == null || str == undefined) return false;
  var flag = -1;
  for (var i = 0;i < this.names.length;++i)
  	if (this.names[i] == str && !this.setup[i]){
  		flag = i;
		str = str + '(' + this.nums[i] + ')';
  		this.setup[i] = true;
  		break;
  	}
  
  if (flag == -1) {
  		console.log('resource ' + str + ' is not enough');
  		return;
  }
	
  node = new JTopo.Node( str);
  node.type = 'dev';
  node.index = flag;
  node.opindex = this.operatorElements;
  node.textPosition = "Top_Center";
  node.layout = {type: 'tree', direction: 'bottom', width: 22, height: 40};
  if (posx == undefined || posx == null)
  	node.setLocation( Math.random() * 600, Math.random() * 400);
  else
  	node.setLocation( posx , posy);
  node.setImage(DevPic[dev], true);
  this.scene.add(node);

  node.ports = new Array();
  for (var i = 0;i < this.ports[flag].length;++i)
  {
    node.ports[i] = new JTopo.Node( this.ports[flag][i] );
    node.ports[i].opindex = this.operatorElements;
    node.ports[i].type = 'port';
    node.ports[i].index = i;
    node.ports[i].father = node;
    node.ports[i].linked = false;
    node.ports[i].setSize(12,12);
    node.ports[i].setLocation(node.x + DevSize[dev].x / 2 + 5 - (this.ports[flag].length * 11) + i * 22 , node.y + DevSize[dev].y / 2 + 34);
    this.scene.add(new JTopo.Link(node , node.ports[i]));
    this.scene.add(node.ports[i]);
  }
  this.operatorElements++;
}

topo.getDevPos = function(){
	var result = [];
	this.scene.findElements(function(e){
		if (e.type == 'dev')
			result.push( e.x.toString() + ' ' + e.y.toString() );
		return false;
	});
	result = result.toString();
	return result;
}
//20170422去除浏览器缓存影响
topo.restart = function(){
	//zhangdonga 2017/4/22
	if(this.scene == null){
	    this.stage = new JTopo.Stage(this.canvas);
	    this.scene = new JTopo.Scene(this.stage);
	    this.scene.background = 'res/images/bg.jpg';
	    this.addFunc(this.scene);
	}else{
		this.scene.clear();
	}
	this.operatorElements = 0;
	this.left_nums = [];
  	this.right_nums = [];
  	this.left_ports = [];
  	this.right_ports = [];
  	for (var i = 0;i < this.setup.length;++i)
  		this.setup[i] = false;
}

topo.undo = function(){
  this.operatorElements--;
  var that = this;
  var removeList = this.scene.findElements(function(e){return e.opindex == that.operatorElements;});	
  removeList.forEach(function(e){ 
  		if (e.elementType == 'link'){
  			if (e.nodeA) e.nodeA.linked = false;
  			if (e.nodeZ) e.nodeZ.linked = false;
  			that.left_nums.pop();
  			that.right_nums.pop();
  			that.left_ports.pop();
  			that.right_ports.pop();
  		}
  		if (e.type == 'dev'){
  			that.setup[e.index] = false;
  		}
  		that.scene.remove(e); 
  });  
}

topo.submit = function(){

	  //以下是提交连线的结果
	  //发送给后台编辑
	  var position = this.getDevPos();
	  var leftNUM_Str = this.left_nums.join("##");
	  var rightNUM_Str = this.right_nums.join("##");
	  var leftport_Str = this.left_ports.join("##");
	  var rightport_Str = this.right_ports.join("##");

	  console.log(position + ' '+leftNUM_Str + ' '+ rightNUM_Str + ' ' + leftport_Str + ' ' + rightport_Str);
	 
	  topoSend(position , leftNUM_Str , rightNUM_Str , leftport_Str , rightport_Str);
	}

topo.saveToDatabase = function(expTaskOrder){        //TasexpTaskOrderkId为0时是初始拓扑
	
	//开启加载页面
	document.getElementById("cover").style.display = "block";
    document.getElementById("layout").style.display = "block";
	
	var position = this.getDevPos();
	var leftNUM_Str = this.left_nums.join("##");
	var rightNUM_Str = this.right_nums.join("##");
	var leftport_Str = this.left_ports.join("##");
	var rightport_Str = this.right_ports.join("##");

	console.log(position + ' '+leftNUM_Str + ' '+ rightNUM_Str + ' ' + leftport_Str + ' ' + rightport_Str);
	
	topoSaveToDatabase(position , leftNUM_Str , rightNUM_Str , leftport_Str , rightport_Str , expTaskOrder);
}
