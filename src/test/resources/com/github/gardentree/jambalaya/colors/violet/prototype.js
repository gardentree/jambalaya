(function() {
  function Scarecrow() {
      this.constructor = 'c'
  };
  Scarecrow.prototype = {
    proto: 'p'
  }

  var scarecrow = new Scarecrow();
  scarecrow.anomalous = 'a';

  return scarecrow;
})();