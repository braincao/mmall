package com.braincao.mmall.controller.portal;

import com.braincao.mmall.common.ServerResponse;
import com.braincao.mmall.service.IProductService;
import com.braincao.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 产品detail
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    /**
     * 前台产品搜索及动态排序List产品:通过categoryId、keyword等搜索，且结果进行分页
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getList(@RequestParam(value = "categoryId", required = false)Integer categoryId,
                                            @RequestParam(value = "keyword", required = false)String keyword,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.searchProductListByIdOrKeyword(categoryId, keyword, pageNum, pageSize, orderBy);
    }

}
