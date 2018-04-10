package spittr.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import spittr.data.SpittleRepository;
import spittr.entity.Spittle;
import spittr.exception.DuplicateSpittleException;
import spittr.exception.SpittleNotFoundException;

@Controller
@RequestMapping("/spittles")
public class SpittleController {

	private static final String MAX_LONG_AS_STRING = Long.toString(Long.MAX_VALUE);
	private SpittleRepository spittleRepository;

	@Autowired
	public SpittleController(SpittleRepository spittleRepository) {
		this.spittleRepository = spittleRepository;
	}
	
	/*@RequestMapping(method=RequestMethod.GET)
	public String spittles(Model model){
		model.addAttribute(spittleRepository.findSpittles(Long.MAX_VALUE, 20));
		return "spittles";
	}*/
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Spittle> spittles(
			@RequestParam(value="max",defaultValue="1000000000") long max,
			@RequestParam(value="count",defaultValue="20") int count){
		return spittleRepository.findSpittles(max, count);
	}
	
	@RequestMapping(value="/{spittleId}",method=RequestMethod.GET)
	public String showSpittle(
			@PathVariable("spittleId") long spittleId,
			Model model) throws SpittleNotFoundException{
		Spittle spittle = spittleRepository.findOne(spittleId);
		if(spittle == null){
			throw new SpittleNotFoundException();
		}
		model.addAttribute(spittle);
		return "spittle";
	}
	
	public String savaSpittle(Spittle spittle,Model model) throws DuplicateSpittleException{
		spittleRepository.save(spittle);
		model.addAttribute("max", 100000);
		model.addAttribute("count", 20);
		return "redirect:/spittles";
	}
}
