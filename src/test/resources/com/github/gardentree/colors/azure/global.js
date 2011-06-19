(function(exports) {
  exports.before = function() {}

  function local() {}

  for (var key in exports) {
    this[key] = exports[key];
  }

  exports.after = function(){}

  return exports;
})({});
